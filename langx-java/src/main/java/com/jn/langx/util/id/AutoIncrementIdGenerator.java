package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;

import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("ALL")
public class AutoIncrementIdGenerator implements IdGenerator {
    private final AtomicLong value;

    public AutoIncrementIdGenerator() {
        this(0);
    }

    public AutoIncrementIdGenerator(int basic) {
        value = new AtomicLong(basic);
    }

    @Override
    public String get(Object object) {
        return value.getAndIncrement() + "";
    }

    @Override
    public String get() {
        return get(null);
    }
}
