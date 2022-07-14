package com.jn.langx.util.timing.scheduling;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.exception.ErrorHandler;
import com.jn.langx.exception.ErrorHandlers;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.SystemPropertys;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.timing.timer.*;
import com.jn.langx.util.timing.timer.immediate.ImmediateTimer;
import com.jn.langx.util.timing.timer.javatimer.JavaTimer;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @since 4.6.3
 */
public class ScheduledExecutors {
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
        return scheduleTask(getScheduledExecutor(), task, trigger, errorHandler);
    }

    /**
     * @since 4.6.4
     * <p>
     * 基于 ScheduledExecutorService来创建定时任务
     */
    public static ScheduledFuture scheduleTask(ScheduledExecutorService scheduledExecutorService, @NonNull Runnable task, @Nullable Trigger trigger, @Nullable ErrorHandler errorHandler) {
        Preconditions.checkNotNull(task);
        if (errorHandler == null) {
            errorHandler = ErrorHandlers.getIgnoreErrorHandler();
        }
        if (trigger == null) {
            trigger = ImmediateTrigger.INSTANCE;
        }
        ReschedulingRunnable taskWrapper = new ReschedulingRunnable(scheduledExecutorService, task, trigger, errorHandler);
        taskWrapper.schedule();
        return taskWrapper;
    }

    /**
     * @since 4.6.4
     * <p>
     * 基于 Timer 来创建定时任务
     */
    public static Timeout timeoutTask(@NonNull Timer timer, @NonNull Runnable task, @Nullable Trigger trigger, @Nullable ErrorHandler errorHandler) {
        return timeoutTask(timer, new RunnableToTimerTaskAdapter(Preconditions.checkNotNull(task, "task is required")), trigger, errorHandler);
    }

    /**
     * @since 4.6.4
     * <p>
     * 基于 Timer 来创建定时任务
     */
    public static Timeout timeoutTask(@NonNull Timer timer, @NonNull TimerTask task, @Nullable Trigger trigger, @Nullable ErrorHandler errorHandler) {
        Preconditions.checkNotNull(task);
        if (errorHandler == null) {
            errorHandler = ErrorHandlers.getIgnoreErrorHandler();
        }
        if (trigger == null) {
            trigger = ImmediateTrigger.INSTANCE;
        }
        ReschedulingTask taskWrapper = new ReschedulingTask(timer, task, trigger, errorHandler);
        taskWrapper.schedule();
        return taskWrapper;
    }

    /**
     * @since 4.6.5
     * <p>
     * 基于 java.util.Timer 来创建定时任务
     */
    public static Timeout javaTimeoutTask(java.util.Timer timer, Executor taskExecutor, Runnable task, Trigger trigger, @Nullable ErrorHandler errorHandler) {
        return timeoutTask(new JavaTimer(timer, taskExecutor), task, trigger, errorHandler);
    }

    /**
     * @since 4.6.5
     * <p>
     * 基于 java.util.Timer 来创建定时任务
     */
    public static Timeout javaTimeoutTask(java.util.Timer timer, Executor taskExecutor, TimerTask task, Trigger trigger, @Nullable ErrorHandler errorHandler) {
        return timeoutTask(new JavaTimer(timer, taskExecutor), task, trigger, errorHandler);
    }

    /**
     * @since 4.6.13
     * <p>
     * 基于 immediate timer 来创建定时任务
     */
    public static Timeout immediateTask(ImmediateTimer timer, TimerTask task, @Nullable ErrorHandler errorHandler) {
        return timeoutTask(timer, task, ImmediateTrigger.INSTANCE, errorHandler);
    }

    /**
     * @since 4.6.13
     * <p>
     * 基于 immediate timer 来创建定时任务
     */
    public static Timeout immediateTask(Executor executor, TimerTask task, @Nullable ErrorHandler errorHandler) {
        return immediateTask(new ImmediateTimer(executor) , task, errorHandler);
    }

}
