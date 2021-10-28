package com.jn.langx.cache;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.lifecycle.Lifecycle;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;

import java.util.concurrent.TimeUnit;

public abstract class BaseCache <K, V> implements Cache<K,V>, Lifecycle {
    // unit: mills
    protected volatile long evictExpiredInterval;
    // unit: mills
    protected volatile long nextEvictExpiredTime;
    // unit: mills
    protected volatile long nextRefreshAllTime;
    // unit: mills，大于 0 时有效
    protected volatile long refreshAllInterval;
    protected Holder<Timeout> refreshAllTimeoutHolder = new Holder<Timeout>();
    protected Timer timer;
    private boolean shutdownTimerSelf = false;
    protected volatile boolean running = false;

    void computeNextEvictExpiredTime() {
        if (evictExpiredInterval < 0) {
            nextEvictExpiredTime = Long.MAX_VALUE;
        } else {
            nextEvictExpiredTime = Dates.nextTime(evictExpiredInterval);
        }
    }

    void computeNextRefreshAllTime() {
        computeNextRefreshAllTime(this.refreshAllInterval);
    }

    void computeNextRefreshAllTime(long refreshAllInterval) {
        if (refreshAllInterval < 0) {
            nextRefreshAllTime = Long.MAX_VALUE;
        } else {
            nextRefreshAllTime = Dates.nextTime(refreshAllInterval);
        }
    }
    class RefreshAllTask implements TimerTask {
        private boolean fixedRate;
        private long delayInMills;

        public RefreshAllTask(boolean fixedRate, long delayInMills) {
            this.fixedRate = fixedRate;
            this.delayInMills = delayInMills;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            if (timeout.isCancelled()) {
                // NOOP
            } else {
                refreshAllAsync(timeout);
                computeNextRefreshAllTime(delayInMills);
                if (fixedRate && !timeout.isCancelled()) {
                    refreshAllTimeoutHolder.set(timer.newTimeout(this, nextRefreshAllTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
                }
            }
        }
    }

    Timeout createRefreshAllTask(int delaySeconds, boolean fixedRate) {
        long delayMills = TimeUnit.SECONDS.toMillis(delaySeconds);
        Timeout timeout = timer.newTimeout(new RefreshAllTask(fixedRate, delayMills), delayMills, TimeUnit.MILLISECONDS);
        return timeout;
    }

    /**
     * @param delay 延迟时间，delay <=0 代表立即刷新， 大于0则代表延迟刷新，单位是 s
     * @param fixed 是否为固定频率的（周期）刷新，若为true，则delay 必须 >0
     * @since 4.0.4
     */
    public void refreshAllAsync(int delay, boolean fixed) {
        Preconditions.checkNotNull(timer, "");
        if (fixed) {
            Preconditions.checkTrue(delay > 0);
        }
        delay = Maths.max(delay, 0);
        if (!fixed && delay == 0) {
            refreshAllAsync(null);
        } else {
            Timeout timeout = createRefreshAllTask(delay, fixed);
            if (fixed) {
                refreshAllTimeoutHolder.set(timeout);
            }
        }
    }

    /**
     * @param timeout
     * @since 4.0.4
     */
    protected abstract void refreshAllAsync(@Nullable final Timeout timeout);

    /**
     * @since 4.0.4
     */
    @Override
    public void cancelRefreshAll() {
        if (!refreshAllTimeoutHolder.isEmpty()) {
            Timeout timeout = refreshAllTimeoutHolder.get();
            timeout.cancel();
        }
    }

    class EvictExpiredTask implements TimerTask {
        @Override
        public void run(Timeout timeout) throws Exception {
            if (timeout.isCancelled()) {
                // NOOP
            } else {
                evictExpired();
                timer.newTimeout(this, nextEvictExpiredTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public void startup() {
        if (!running) {
            running = true;
            computeNextEvictExpiredTime();
            computeNextRefreshAllTime();
            if (evictExpiredInterval > 0 || refreshAllInterval > 0) {
                if (timer == null) {
                    timer = new HashedWheelTimer(new CommonThreadFactory("Cache-Evict", false));
                    shutdownTimerSelf = true;
                }
            }
            if (evictExpiredInterval > 0) {
                timer.newTimeout(this.new EvictExpiredTask(), nextEvictExpiredTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            }
            if (refreshAllInterval > 0) {
                refreshAllTimeoutHolder.set(timer.newTimeout(this.new RefreshAllTask(true, refreshAllInterval), refreshAllInterval, TimeUnit.MILLISECONDS));
            }
        }
    }

    @Override
    public void shutdown() {
        running = false;
        if (timer != null) {
            if (shutdownTimerSelf) {
                timer.stop();
            }
        }
    }
}
