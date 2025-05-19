package com.jn.langx.util.timing.timer;

import com.jn.langx.util.concurrent.CommonThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class WheelTimers {
    private WheelTimers(){

    }
    public static HashedWheelTimer newHashedWheelTimer() {
        return newHashedWheelTimer(null);
    }

    public static HashedWheelTimer newHashedWheelTimer(ThreadFactory threadFactory) {
        return newHashedWheelTimer(threadFactory, 100, TimeUnit.MILLISECONDS);
    }

    public static HashedWheelTimer newHashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        return newHashedWheelTimer(threadFactory, tickDuration, unit, 512);
    }

    public static HashedWheelTimer newHashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel) {
        return newHashedWheelTimer(threadFactory, tickDuration, unit, ticksPerWheel, false);
    }

    public static HashedWheelTimer newHashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection) {
        return new HashedWheelTimer(threadFactory == null ? new CommonThreadFactory() : threadFactory, tickDuration, unit, ticksPerWheel, leakDetection, -1);
    }

    public static HashedWheelTimer newHashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection, Executor executor) {
        return new HashedWheelTimer(threadFactory == null ? new CommonThreadFactory() : threadFactory, tickDuration, unit, ticksPerWheel, leakDetection, -1, executor);
    }
}
