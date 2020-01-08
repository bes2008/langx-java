package com.jn.langx.util.concurrent;

import java.util.concurrent.Callable;

public class WrappedCallable<V> extends WrappedTask<V> implements Callable<V> {

    private Callable<V> task;

    public WrappedCallable() {

    }

    public void setTask(Callable<V> task) {
        this.task = task;
    }

    public WrappedCallable(Callable<V> callable) {
        this.task = callable;
    }

    @Override
    public V call() throws Exception {
        return runInternal();
    }

    @Override
    protected V run0() throws Exception {
        if (task != null) {
            return task.call();
        }
        return null;
    }
}
