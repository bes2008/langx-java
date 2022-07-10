package com.jn.langx.util.concurrent;

import com.jn.langx.util.Throwables;
import com.jn.langx.util.logging.Loggers;

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

    public static <V> CommonTask<V> wrap(Runnable r) {
        if (r instanceof CommonTask) {
            return (CommonTask) r;
        }
        return new CommonTask<V>(r);
    }

    public static <V> CommonTask<V> wrap(Callable<V> r) {
        if (r instanceof CommonTask) {
            return (CommonTask) r;
        }
        return new CommonTask<V>(r);
    }


    @Override
    public V call() throws Exception {
        try {
            return t0.call();
        } catch (Exception e) {
            Loggers.getLogger(CommonTask.class).error(e.getMessage(), e);
            throw e;
        } catch (Throwable e1) {
            Loggers.getLogger(CommonTask.class).error(e1.getMessage(), e1);
            throw Throwables.wrapAsRuntimeException(e1);
        }
    }

    @Override
    public void run() {
        try {
            t1.run();
        } catch (Throwable e1) {
            Loggers.getLogger(CommonTask.class).error(e1.getMessage(), e1);
            throw Throwables.wrapAsRuntimeException(e1);
        }
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
