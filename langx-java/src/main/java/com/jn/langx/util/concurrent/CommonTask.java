package com.jn.langx.util.concurrent;

import java.util.concurrent.Callable;

public class CommonTask<V> implements Callable<V>, Runnable {
    private Callable<V> t0;
    private Runnable t1;

    public CommonTask(Callable<V> t) {
        this.t0 = t;
    }

    public CommonTask(Runnable t) {
        this.t1 = t;
    }

    @Override
    public void run() {
        t1.run();
    }

    @Override
    public V call() throws Exception {
        return t0.call();
    }
}
