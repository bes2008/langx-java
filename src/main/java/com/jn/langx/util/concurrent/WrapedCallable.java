package com.jn.langx.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class WrapedCallable<V> extends WrapedTask<V> implements Callable<V> {
    private static final Logger logger = LoggerFactory.getLogger(WrapedCallable.class);

    private Callable<V> task;

    public WrapedCallable() {

    }

    public void setTask(Callable<V> task) {
        this.task = task;
    }

    public WrapedCallable(Callable<V> callable) {
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
