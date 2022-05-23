package com.jn.langx.util.concurrent;

import com.jn.langx.util.os.Platform;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Global {
    private static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(Platform.cpuCore() * 2, new CommonThreadFactory("COMMON_SCHEDULER", true));

    public static ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutorService;
    }
}
