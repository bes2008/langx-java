package com.jn.langx.util.regexp;

import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.timing.clock.Clock;
import com.jn.langx.util.timing.clock.SystemClock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultMatcherWatchdog implements MatcherWatchdog {

    /**
     * 检查超时的间隔
     */
    private final long interval;
    private final long maxExecutionTime;
    private final Clock clock;
    /**
     * 调度代理
     */
    private final Consumer2<Long, Runnable> scheduler;
    private final AtomicInteger registered = new AtomicInteger(0);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ConcurrentHashMap<RegexpMatcher, Long> registry = new ConcurrentHashMap<RegexpMatcher, Long>();

    public DefaultMatcherWatchdog(long intervalInMills, long maxExecutionTime, Consumer2<Long, Runnable> schedulerProxy) {
        this(intervalInMills, maxExecutionTime, new SystemClock(), schedulerProxy);
    }

    public DefaultMatcherWatchdog(long intervalInMills, long maxExecutionTime, Clock clock, Consumer2<Long, Runnable> schedulerProxy) {
        this.interval = intervalInMills;
        this.maxExecutionTime = maxExecutionTime;
        this.clock = clock;
        this.scheduler = schedulerProxy;
    }

    public void register(RegexpMatcher matcher) {
        registered.getAndIncrement();
        Long previousValue = registry.put(matcher, clock.getTime());
        if (running.compareAndSet(false, true)) {
            scheduler.accept(interval, createInterruptTask());
        }
        assert previousValue == null;
    }

    @Override
    public long maxExecutionTimeInMillis() {
        return maxExecutionTime;
    }

    public void unregister(RegexpMatcher matcher) {
        Long previousValue = registry.remove(matcher);
        registered.decrementAndGet();
    }

    private void interruptLongRunningExecutions() {
        final long currentTime = clock.getTime();
        for (Map.Entry<RegexpMatcher, Long> entry : registry.entrySet()) {
            long deadline = entry.getValue();
            if ((currentTime - deadline) > maxExecutionTime) {
                entry.getKey().interrupt();
            }
        }
        if (registered.get() > 0) {
            scheduler.accept(interval, createInterruptTask());
        } else {
            running.set(false);
        }
    }

    private Runnable createInterruptTask() {
        return new Runnable() {
            @Override
            public void run() {
                interruptLongRunningExecutions();
            }
        };
    }

}