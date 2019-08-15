package com.jn.langx.util.concurrent;

import com.jn.langx.factory.Factory;
import com.jn.langx.util.Strings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jinuo.fang
 */
public class CommonThreadFactory implements ThreadFactory, Factory<Runnable,Thread> {
    // key: prefix
    // value: thread N.O.
    private static final Map<String, AtomicLong> threadNumber = new ConcurrentHashMap<String, AtomicLong>();
    // key: prefix
    // value: factory N.O.
    private static final Map<String, Integer> factoryToNumber = new ConcurrentHashMap<String, Integer>();
    private static AtomicInteger factoryNumber = new AtomicInteger(0);

    private boolean daemon;
    private String prefix;
    private int factoryNo = 0;

    public CommonThreadFactory() {
        this("COMMON", false);
    }

    public CommonThreadFactory(String prefix, boolean daemon) {
        if (Strings.isBlank(prefix)) {
            prefix = "COMMON";
        }
        this.daemon = daemon;
        this.prefix = prefix;

        if (threadNumber.containsKey(prefix)) {
            factoryNo = factoryToNumber.get(prefix);
        } else {
            factoryNo = factoryNumber.getAndIncrement();
            factoryToNumber.put(prefix, factoryNo);
        }
    }

    private String nextThreadName() {
        return new StringBuilder(prefix).append("(").append(factoryNo).append("-").append(threadNumber.get(prefix).getAndIncrement()).append(")").toString();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new WrappedThread(r, nextThreadName());
        thread.setDaemon(daemon);
        return thread;
    }

    @Override
    public Thread get(Runnable runnable) {
        return newThread(runnable);
    }

    public static ThreadFactory create(String prefix, boolean daemon) {
        return new CommonThreadFactory(prefix, daemon);
    }

    public static ThreadFactory create(String prefix) {
        return new CommonThreadFactory(prefix, false);
    }
}
