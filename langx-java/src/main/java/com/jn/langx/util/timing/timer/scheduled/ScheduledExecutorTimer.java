package com.jn.langx.util.timing.timer.scheduled;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Sets;
import com.jn.langx.util.timing.timer.AbstractTimer;
import com.jn.langx.util.timing.timer.RunnableToTimerTaskAdapter;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.TimerTask;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @since 4.6.4
 */
public class ScheduledExecutorTimer extends AbstractTimer {
    private ScheduledExecutorService scheduledExecutor;

    public ScheduledExecutorTimer(ScheduledExecutorService scheduledExecutor, Executor taskExecutor) {
        this.scheduledExecutor = scheduledExecutor;
        setTaskExecutor(taskExecutor);
        this.running = true;
    }

    @Override
    public Timeout newTimeout(Runnable task, long delay, TimeUnit unit) {
        Preconditions.checkNotNull(task, "task is required");
        TimerTask tt = new RunnableToTimerTaskAdapter(task);
        return this.newTimeout(tt, delay, unit);
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

        final ScheduledTimeout timeout = new ScheduledTimeout(this, task, deadlineInMills);
        ScheduledFuture scheduledFuture = this.scheduledExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                timeout.executeTask();
            }
        }, delay, unit);
        timeout.setFuture(scheduledFuture);
        return timeout;
    }


    @Override
    public Set<Timeout> stop() {
        this.running = false;
        scheduledExecutor.shutdown();
        return Sets.newHashSet();
    }


}
