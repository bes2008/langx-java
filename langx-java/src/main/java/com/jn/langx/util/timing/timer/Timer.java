package com.jn.langx.util.timing.timer;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
/**
 * Schedules {@link TimerTask}s for one-time future execution in a background
 * thread.
 */
public interface Timer {

    /**
     * Schedules the specified {@link Runnable} task for one-time execution after
     * the specified delay.
     *
     * @param task   the task to schedule
     * @param delay  the delay from now to execution in the given time unit
     * @param unit   the time unit of the delay argument
     * @return a handle which is associated with the specified task
     * @throws IllegalStateException      if this timer has been {@linkplain #stop() stopped} already
     * @throws RejectedExecutionException if the pending timeouts are too many and creating new timeout
     *                                    can cause instability in the system.
     */
    Timeout newTimeout(Runnable task, long delay, TimeUnit unit);

    /**
     * Schedules the specified {@link TimerTask} for one-time execution after
     * the specified delay.
     *
     * @param task   the task to schedule
     * @param delay  the delay from now to execution in the given time unit
     * @param unit   the time unit of the delay argument
     * @return a handle which is associated with the specified task
     * @throws IllegalStateException      if this timer has been {@linkplain #stop() stopped} already
     * @throws RejectedExecutionException if the pending timeouts are too many and creating new timeout
     *                                    can cause instability in the system.
     */
    Timeout newTimeout(TimerTask task, long delay, TimeUnit unit);

    /**
     * Releases all resources acquired by this {@link Timer} and cancels all
     * tasks which were scheduled but not executed yet.
     *
     * @return the handles associated with the tasks which were canceled by
     * this method
     */
    Set<Timeout> stop();

    /**
     * Checks if this {@link Timer} supports distinct tasks.
     *
     * @return {@code true} if distinct tasks are supported, otherwise {@code false}
     */
    boolean isDistinctSupported();

    /**
     * Returns the {@link Executor} which is used to execute the scheduled tasks.
     *
     * @return the task executor
     * @since 4.6.4
     */
    Executor getTaskExecutor();

    /**
     * Checks if this {@link Timer} is currently running.
     *
     * @return {@code true} if this timer is running, otherwise {@code false}
     * @since 4.6.14
     */
    boolean isRunning();
}
