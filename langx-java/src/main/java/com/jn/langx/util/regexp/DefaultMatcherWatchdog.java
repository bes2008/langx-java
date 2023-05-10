package com.jn.langx.util.regexp;

import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.supplier.LongSupplier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultMatcherWatchdog implements MatcherWatchdog {

    private final long interval;
    private final long maxExecutionTime;
    private final LongSupplier relativeTimeSupplier;
    private final Consumer2<Long, Runnable> scheduler;
    private final AtomicInteger registered = new AtomicInteger(0);
    private final AtomicBoolean running = new AtomicBoolean(false);
    final ConcurrentHashMap<RegexpMatcher, Long> registry = new ConcurrentHashMap<RegexpMatcher, Long>();

    public DefaultMatcherWatchdog(long interval, long maxExecutionTime, LongSupplier relativeTimeSupplier, Consumer2<Long, Runnable> scheduler) {
        this.interval = interval;
        this.maxExecutionTime = maxExecutionTime;
        this.relativeTimeSupplier = relativeTimeSupplier;
        this.scheduler = scheduler;
    }

    public void register(RegexpMatcher matcher) {
        registered.getAndIncrement();
        Long previousValue = registry.put(matcher, relativeTimeSupplier.getAsLong());
        if (running.compareAndSet(false, true)) {
            scheduler.accept(interval,createInterruptTask());
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
        assert previousValue != null;
    }

    private void interruptLongRunningExecutions() {
        final long currentRelativeTime = relativeTimeSupplier.getAsLong();
        for (Map.Entry<RegexpMatcher, Long> entry : registry.entrySet()) {
            if ((currentRelativeTime - entry.getValue()) > maxExecutionTime) {
                entry.getKey().interrupt();
                // not removing the entry here, this happens in the unregister() method.
            }
        }
        if (registered.get() > 0) {
            scheduler.accept(interval, createInterruptTask());
        } else {
            running.set(false);
        }
    }

    private Runnable createInterruptTask(){
       return new Runnable() {
            @Override
            public void run() {
                interruptLongRunningExecutions();
            }
        };
    }

}