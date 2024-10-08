package com.jn.langx.util.timing.timer;

import com.jn.langx.util.Objs;
import com.jn.langx.util.concurrent.executor.ImmediateExecutor;

import java.util.concurrent.Executor;

public abstract class AbstractTimer implements Timer {
    protected Executor taskExecutor;
    protected volatile boolean running;

    protected void setTaskExecutor(Executor e){
        this.taskExecutor = Objs.useValueIfEmpty(e, ImmediateExecutor.INSTANCE);
    }

    @Override
    public Executor getTaskExecutor() {
        return taskExecutor;
    }

    @Override
    public boolean isDistinctSupported() {
        return false;
    }

    /**
     * @since 4.6.14
     */
    @Override
    public boolean isRunning() {
        return running;
    }
}
