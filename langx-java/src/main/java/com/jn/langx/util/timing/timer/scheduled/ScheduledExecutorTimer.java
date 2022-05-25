package com.jn.langx.util.timing.timer.scheduled;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.TimeoutFactory;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @since 4.6.4
 */
public class ScheduledExecutorTimer implements Timer {
    private ScheduledExecutorService scheduledExecutor;
    private Executor taskExecutor;
    private TimeoutFactory<ScheduledExecutorTimer, ScheduledTimeout> scheduledTimeoutFactory = new ScheduledTimeoutFactory();

    public ScheduledExecutorTimer(ScheduledExecutorService scheduledExecutor, Executor taskExecutor) {
        this.scheduledExecutor = scheduledExecutor;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public Timeout newTimeout(@NonNull final TimerTask task, long delay, @NonNull TimeUnit unit) {
        if (task == null || unit == null) {
            throw new NullPointerException();
        }
        if (delay < 0) {
            delay = 0;
        }

        long deadlineInMills = System.currentTimeMillis() + unit.toMillis(delay);

        final ScheduledTimeout timeout = scheduledTimeoutFactory.create(this, task, deadlineInMills);
        ScheduledFuture scheduledFuture = this.scheduledExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                timeout.executeTask();
            }
        }, delay, unit);
        timeout.setFuture(scheduledFuture);
        return timeout;
    }

    public Executor getTaskExecutor() {
        return taskExecutor;
    }

    @Override
    public Set<Timeout> stop() {
        scheduledExecutor.shutdown();
        return null;
    }

    @Override
    public boolean isDistinctSupported() {
        return false;
    }


}
