package com.jn.langx.util.timing.timer.javatimer;

import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class JavaTimer implements Timer {
    /**
     * 由 一个线程 + 一个Queue 结合完成的任务执行器
     */
    private java.util.Timer jtimer;
    private Executor taskExecutor;


    public JavaTimer(java.util.Timer timer, Executor executor) {
        this.jtimer = timer;
        this.taskExecutor = executor;
    }

    @Override
    public Timeout newTimeout(Runnable task, long delay, TimeUnit unit) {
        return null;
    }

    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        long delayInMills = unit.toMillis(delay);
        long deadline = System.currentTimeMillis() + delayInMills;

        final JavaTimeout timeout = new JavaTimeout(this, task, deadline);
        java.util.TimerTask timerRef = new java.util.TimerTask() {
            @Override
            public void run() {
                timeout.executeTask();
            }
        };
        timeout.setTaskRef(timerRef);
        jtimer.schedule(timerRef, delayInMills);
        return timeout;
    }

    @Override
    public Set<Timeout> stop() {
        return null;
    }

    @Override
    public boolean isDistinctSupported() {
        return false;
    }

    @Override
    public Executor getTaskExecutor() {
        return this.taskExecutor;
    }
}
