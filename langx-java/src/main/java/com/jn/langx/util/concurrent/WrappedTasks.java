package com.jn.langx.util.concurrent;

import com.jn.langx.util.Preconditions;

import java.util.concurrent.Callable;

/**
 * @author jinuo.fang
 *
 * @see CommonTask
 */
@Deprecated
public class WrappedTasks {
    public static WrappedRunnable wrap(Runnable target) {
        Preconditions.checkNotNull(target);
        return ((target instanceof WrappedRunnable) ? (WrappedRunnable) target : new WrappedRunnable(target));
    }

    public static <V> WrappedCallable<V> wrap(Callable<V> target) {
        Preconditions.checkNotNull(target);
        return ((target instanceof WrappedCallable) ? (WrappedCallable) target : new WrappedCallable(target));
    }
}
