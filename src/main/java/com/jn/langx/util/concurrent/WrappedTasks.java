package com.jn.langx.util.concurrent;

import java.util.concurrent.Callable;

public class WrappedTasks {
    public static WrappedRunable wrap(Runnable target) {
        if (target == null) {
            return null;
        }
        return ((target instanceof WrappedRunable) ? (WrappedRunable) target : new WrappedRunable(target));
    }

    public static <V> WrappedCallable<V> wrap(Callable<V> target) {
        if (target == null) {
            return null;
        }
        return ((target instanceof WrappedCallable) ? (WrappedCallable) target : new WrappedCallable(target));
    }
}
