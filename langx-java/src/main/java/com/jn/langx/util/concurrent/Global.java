package com.jn.langx.util.concurrent;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.exception.ErrorHandler;
import com.jn.langx.exception.ErrorHandlers;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.timing.scheduling.ImmediateTrigger;
import com.jn.langx.util.timing.scheduling.ReschedulingRunnable;
import com.jn.langx.util.timing.scheduling.Trigger;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Global {
    private static int schedulerCoreSize;
    private static String schedulerThreadPrefix;
    private static ScheduledExecutorService scheduledExecutorService;

    static {
        schedulerCoreSize = SystemPropertys.getAccessor().getInteger("langx.global.schedulerCoreSize", Platform.cpuCore() * 2);
        schedulerThreadPrefix = SystemPropertys.getAccessor().getString("langx.global.schedulerThreadPrefix", "GLOBAL_SCHEDULER");
        scheduledExecutorService = new ScheduledThreadPoolExecutor(schedulerCoreSize, new CommonThreadFactory(schedulerThreadPrefix, true));
    }

    public static ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutorService;
    }

    public static ScheduledFuture scheduleTask(@NonNull Runnable task) {
        return scheduleTask(task, null, null);
    }

    public static ScheduledFuture scheduleTask(@NonNull Runnable task, @NonNull ErrorHandler errorHandler) {
        return scheduleTask(task, null, errorHandler);
    }

    public static ScheduledFuture scheduleTask(@NonNull Runnable task, @Nullable Trigger trigger, @Nullable ErrorHandler errorHandler) {
        Preconditions.checkNotNull(task);
        if (errorHandler == null) {
            errorHandler = ErrorHandlers.getIgnoreErrorHandler();
        }
        if (trigger == null) {
            trigger = ImmediateTrigger.INSTANCE;
        }
        ReschedulingRunnable taskWrapper = new ReschedulingRunnable(task, trigger, getScheduledExecutor(), errorHandler);
        taskWrapper.schedule();
        return taskWrapper;
    }
}
