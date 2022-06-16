package com.jn.langx.util.concurrent;

import com.jn.langx.util.Throwables;

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
    public V call() throws Exception {
        return t0.call();
    }

    @Override
    public void run() {
        t1.run();
    }

    public Class getExpectClass() {
        if (t0 != null) {
            return Callable.class;
        }
        return Runnable.class;
    }

    public static class RunnableToCallable<V> implements Callable<V> {
        private Runnable runnable;
        private V expectedResult;

        public RunnableToCallable(Runnable runnable) {
            this(runnable, null);
        }

        public RunnableToCallable(Runnable runnable, V expectedResult) {
            this.runnable = runnable;
            this.expectedResult = expectedResult;
        }

        @Override
        public V call() throws Exception {
            runnable.run();
            return expectedResult;
        }
    }

    public static class CallableToRunnable implements Runnable {
        private Callable callable;

        public CallableToRunnable(Callable callable) {
            this.callable = callable;
        }

        @Override
        public void run() {
            try {
                callable.call();
            } catch (Throwable ex) {
                throw Throwables.wrapAsRuntimeException(ex);
            }
        }
    }
}
