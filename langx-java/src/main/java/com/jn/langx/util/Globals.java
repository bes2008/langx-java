package com.jn.langx.util;

import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.WheelTimers;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Globals {
    private static final Object lock = new Object();
    private static HashedWheelTimer wheelTimer;

    public static HashedWheelTimer getWheelTimer() {
        if (wheelTimer == null) {
            synchronized (lock) {
                if (wheelTimer == null) {
                    Executor executor = new ThreadPoolExecutor(0, 100, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new CommonThreadFactory("Global-WheelTimer-TimeoutTask", true));
                    wheelTimer = WheelTimers.newHashedWheelTimer(new CommonThreadFactory("Global-WheelTimer-Worker", true), 100, TimeUnit.MILLISECONDS, 600, false, executor);
                }
            }
        }
        return wheelTimer;
    }
}
