package com.jn.langx.util.concurrent;

import java.util.concurrent.Callable;

public class WrapedTasks {
    public static WrapedRunable wrap(Runnable target) {
        if (target == null) {
            return null;
        }
        return ((target instanceof WrapedRunable) ? (WrapedRunable) target : new WrapedRunable(target));
    }

    public static <V> WrapedCallable<V> wrap(Callable<V> target) {
        if (target == null) {
            return null;
        }
        return ((target instanceof WrapedCallable) ? (WrapedCallable) target : new WrapedCallable(target));
    }
}
