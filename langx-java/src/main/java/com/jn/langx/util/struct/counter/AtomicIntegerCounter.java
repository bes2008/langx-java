package com.jn.langx.util.struct.counter;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerCounter implements Counter<Integer> {
    private AtomicInteger vh;

    public AtomicIntegerCounter() {
        this(0);
    }

    public AtomicIntegerCounter(int init) {
        this.vh = new AtomicInteger(init);
    }

    @Override
    public Integer increment() {
        return increment(1);
    }

    @Override
    public Integer increment(Integer delta) {
        return vh.addAndGet(delta);
    }

    @Override
    public Integer decrement() {
        return decrement(1);
    }

    @Override
    public Integer decrement(Integer delta) {
        return increment(-delta);
    }

    @Override
    public Integer getAndIncrement() {
        return getAndIncrement(1);
    }

    @Override
    public Integer getAndIncrement(Integer delta) {
        return vh.getAndAdd(delta);
    }

    @Override
    public Integer get() {
        return vh.get();
    }

    @Override
    public void set(Integer value) {
        vh.set(value);
    }
}
