package com.jn.langx.util.timing.timer.javatimer;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.timing.timer.RunnableToTimerTaskAdapter;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @since 4.6.5
 */
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
        return newTimeout(new RunnableToTimerTaskAdapter(task), delay, unit);
    }

    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        long delayInMills = unit.toMillis(delay);
        long deadline = System.currentTimeMillis() + delayInMills;

        final JavaTimeout timeout = new JavaTimeout(this, task, deadline);
        java.util.TimerTask taskRef = new java.util.TimerTask() {
            @Override
            public void run() {
                timeout.executeTask();
            }
        };
        timeout.setTaskRef(taskRef);
        jtimer.schedule(taskRef, delayInMills);
        return timeout;
    }

    @Override
    public Set<Timeout> stop() {
        // 调用 cancel 方法后，queue会自动清空， thread 会自动停止。
        jtimer.cancel();
        return Collects.emptyHashSet();
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
