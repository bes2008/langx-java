package com.jn.langx.util.concurrent;

import java.util.concurrent.Callable;

/**
 * @author jinuo.fang
 */
public class WrappedTasks {
    public static WrappedRunnable wrap(Runnable target) {
        if (target == null) {
            return null;
        }
        return ((target instanceof WrappedRunnable) ? (WrappedRunnable) target : new WrappedRunnable(target));
    }

    public static <V> WrappedCallable<V> wrap(Callable<V> target) {
        if (target == null) {
            return null;
        }
        return ((target instanceof WrappedCallable) ? (WrappedCallable) target : new WrappedCallable(target));
    }
}
