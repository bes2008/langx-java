package com.jn.langx.util.timing.timer;

import com.jn.langx.util.concurrent.CommonThreadFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class WheelTimers {

    public static HashedWheelTimer newHashedWheelTimer() {
        return newHashedWheelTimer(null);
    }

    public static HashedWheelTimer newHashedWheelTimer(ThreadFactory threadFactory) {
        return new HashedWheelTimer(threadFactory);
    }

    public static HashedWheelTimer newHashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        return new HashedWheelTimer(threadFactory, tickDuration, unit, 512);
    }

    public static HashedWheelTimer newHashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel) {
        return new HashedWheelTimer(threadFactory, tickDuration, unit, ticksPerWheel, false);
    }

    public static HashedWheelTimer newHashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection) {
        return new HashedWheelTimer(threadFactory == null ? new CommonThreadFactory() : threadFactory, tickDuration, unit, ticksPerWheel, leakDetection, -1);
    }
}
