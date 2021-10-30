package com.jn.langx.cache;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.lifecycle.Lifecycle;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class BaseCache<K, V> implements Cache<K, V>, Lifecycle {
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
    /**
     * 刷新时去重。
     * 当刷新的任务执行很慢时，很有可能出现数据堆积，就需要保证再添加刷新任务时，发现任务队列（队列在timer 的taskExecutor中）中有重复Key时，不将新的刷新任务添加到队列中.
     * <p>
     * 如果启用了去重功能，需要注意如下要点：
     * <pre>
     * 1） HashedWheelTimer 需要使用 DistinctHashedWheelTimeoutFactory
     * 2） 创建Cache对象时，设置 distinctWhenRefresh = true
     * 3） 放入Cache中的 Key 需要重写 equals(), hashCode()
     * </pre>
     */
    private boolean distinctWhenRefresh = false;

    public void setDistinctWhenRefresh(boolean distinctWhenRefresh) {
        this.distinctWhenRefresh = distinctWhenRefresh;
    }

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

    /**
     * @since 4.0.5
     */
    class RefreshKeyTask implements TimerTask {
        private K key;

        RefreshKeyTask(K key) {
            this.key = key;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            if (timeout.isCancelled()) {
                // noop
            } else {
                refresh(key);
            }
        }

        public boolean equals(Object obj) {
            if (!distinctWhenRefresh) {
                return false;
            }
            if (obj == key) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            RefreshKeyTask that = (RefreshKeyTask) obj;
            if (!Reflects.isSubClassOrEquals(key.getClass(), that.key.getClass())) {
                return false;
            }
            return Objs.equals(that.key, key);
        }

        @Override
        public int hashCode() {
            return Objs.hash(key);
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
    protected void refreshAllAsync(@Nullable final Timeout timeout) {
        Set<K> keys = keys();
        Collects.forEach(keys, new Consumer<K>() {
            @Override
            public void accept(K key) {
                /**
                 * @since 4.0.5
                 * 利用 timer 可以再次 异步执行
                 */
                if (timer != null) {
                    timer.newTimeout(new AbstractCache.RefreshKeyTask(key), 1, TimeUnit.MILLISECONDS);
                } else {
                    refresh(key);
                }
            }
        }, new Predicate<K>() {
            @Override
            public boolean test(K key) {
                if (timeout != null && timeout.isCancelled()) {
                    return true;
                }
                return false;
            }
        });
    }

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
