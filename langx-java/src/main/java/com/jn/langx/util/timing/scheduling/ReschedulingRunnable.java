package com.jn.langx.util.timing.scheduling;

import com.jn.langx.exception.ErrorHandler;
import com.jn.langx.util.Preconditions;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @see com.jn.langx.util.timing.timer.ReschedulingTask
 * @since 4.6.3
 */
public class ReschedulingRunnable implements ScheduledFuture<Object>, Runnable {

    private final Trigger trigger;

    private final SimpleTriggerContext triggerContext = new SimpleTriggerContext();
    private volatile ScheduledFuture currentFuture;
    private ScheduledExecutorService executor;
    private volatile Date scheduledExecutionTime;
    private final Runnable delegateTask;
    private final ErrorHandler errorHandler;

    private final Object triggerContextMonitor = new Object();

    public ReschedulingRunnable(ScheduledExecutorService executor, Runnable delegate, Trigger trigger, ErrorHandler errorHandler) {
        Preconditions.checkNotNull(delegate, "Delegate must not be null");
        Preconditions.checkNotNull(errorHandler, "ErrorHandler must not be null");
        this.delegateTask = delegate;
        this.errorHandler = errorHandler;
        this.trigger = trigger;
        this.executor = executor;
    }

    public ReschedulingRunnable(Runnable delegate, Trigger trigger, ScheduledExecutorService executor, ErrorHandler errorHandler) {
        this(executor, delegate, trigger, errorHandler);
    }


    public ScheduledFuture schedule() {
        synchronized (this.triggerContextMonitor) {
            this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
            if (this.scheduledExecutionTime == null) {
                return null;
            }
            long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();
            if(initialDelay<0){
                initialDelay = 0L;
            }
            this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
            return this;
        }
    }

    @Override
    public void run() {
        Date actualExecutionTime = new Date();
        try {
            this.delegateTask.run();
        } catch (Throwable ex) {
            this.errorHandler.handle(ex);
        }
        Date completionTime = new Date();
        synchronized (this.triggerContextMonitor) {
            this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
        }
        if (!this.currentFuture.isCancelled()) {
            schedule();
        }
    }


    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.currentFuture.cancel(mayInterruptIfRunning);
    }

    public boolean isCancelled() {
        return this.currentFuture.isCancelled();
    }

    public boolean isDone() {
        return this.currentFuture.isDone();
    }

    public Object get() throws InterruptedException, ExecutionException {
        return this.currentFuture.get();
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.currentFuture.get(timeout, unit);
    }

    public long getDelay(TimeUnit unit) {
        return this.currentFuture.getDelay(unit);
    }

    public int compareTo(Delayed other) {
        if (this == other) {
            return 0;
        }
        long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
        return (diff == 0 ? 0 : ((diff < 0) ? -1 : 1));
    }

}
