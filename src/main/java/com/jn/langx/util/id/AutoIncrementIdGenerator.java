package com.jn.langx.util.id;

import com.jn.langx.IdGenerator;

import java.util.concurrent.atomic.AtomicLong;

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
}
