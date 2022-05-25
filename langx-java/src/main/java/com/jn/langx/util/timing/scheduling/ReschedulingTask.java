package com.jn.langx.util.timing.scheduling;

import com.jn.langx.exception.ErrorHandler;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ReschedulingTask implements Timeout, TimerTask {
    private Timeout timeout;
    private Timer timer;
    private final Trigger trigger;
    private final SimpleTriggerContext triggerContext = new SimpleTriggerContext();
    private volatile Date scheduledExecutionTime;
    private final TimerTask delegateTask;
    private final ErrorHandler errorHandler;
    private final Object triggerContextMonitor = new Object();

    public ReschedulingTask(Timer timer, TimerTask task, Trigger trigger, ErrorHandler errorHandler) {
        Preconditions.checkNotNull(task, "task must not be null");
        Preconditions.checkNotNull(errorHandler, "ErrorHandler must not be null");
        this.timer = timer;
        this.delegateTask = task;
        this.trigger = trigger;
        this.errorHandler = errorHandler;
    }


    /**
     * 对象创建完毕后，调用该方法
     * @return 返回timeout
     */
    public Timeout schedule() {
        synchronized (this.triggerContextMonitor) {
            this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
            if (this.scheduledExecutionTime == null) {
                return null;
            }
            long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();
            this.timeout = this.timer.newTimeout(this, initialDelay, TimeUnit.MILLISECONDS);
            return this;
        }
    }

    public void run(Timeout timeout) throws Exception {
        Date actualExecutionTime = new Date();
        try {
            this.delegateTask.run(timeout);
        } catch (Throwable ex) {
            this.errorHandler.handle(ex);
        }
        Date completionTime = new Date();
        synchronized (this.triggerContextMonitor) {
            this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
        }
        if (!this.timeout.isCancelled()) {
            schedule();
        }
    }

    @Override
    public Timer timer() {
        return this.timeout.timer();
    }

    @Override
    public TimerTask task() {
        return this.timeout.task();
    }

    @Override
    public boolean isExpired() {
        return this.timeout.isExpired();
    }

    @Override
    public boolean isCancelled() {
        return this.timeout.isCancelled();
    }

    @Override
    public boolean cancel() {
        return this.timeout.cancel();
    }


}
