package com.jn.langx.util.concurrent;

import com.jn.langx.Factory;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Strings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Assume :
 * 1) one prefix to many factory
 * 2) one factory to many thread
 *
 * @author jinuo.fang
 */
public class CommonThreadFactory implements ThreadFactory, Factory<Runnable, Thread> {

    /**
     * key: prefix
     * value: factory N.O.
     */
    private static final ConcurrentHashMap<String, AtomicInteger> PREFIX_TO_FACTORY = new ConcurrentHashMap<String, AtomicInteger>();
    /**
     * key: factory
     * value: thread N.O.
     */
    private static final Map<Integer, AtomicInteger> FACTORY_TO_THREAD = new ConcurrentHashMap<Integer, AtomicInteger>();

    private boolean daemon;
    private String prefix;
    private int factoryNo;

    public CommonThreadFactory() {
        this("COMMON", false);
    }

    public CommonThreadFactory(String prefix, boolean daemon) {
        if (Strings.isBlank(prefix)) {
            prefix = "COMMON";
        }
        this.daemon = daemon;
        this.prefix = prefix;

        PREFIX_TO_FACTORY.putIfAbsent(prefix, new AtomicInteger(0));
        factoryNo = PREFIX_TO_FACTORY.get(prefix).getAndIncrement();
        FACTORY_TO_THREAD.put(factoryNo, new AtomicInteger(0));
    }

    /**
     * @return ${prefix}(${factoryNo}-${threadNumber})
     */
    private String nextThreadName() {
        return StringTemplates.formatWithPlaceholder("{}({}-{})", prefix, factoryNo, FACTORY_TO_THREAD.get(factoryNo).getAndIncrement());
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new WrappedThread(r, nextThreadName());
        thread.setDaemon(daemon);
        return thread;
    }

    public Thread execute(Runnable r) {
        Thread thread = newThread(r);
        thread.start();
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
