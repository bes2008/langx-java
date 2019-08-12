package com.jn.langx.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class WrappedCallable<V> extends WrappedTask<V> implements Callable<V> {
    private static final Logger logger = LoggerFactory.getLogger(WrappedCallable.class);

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
