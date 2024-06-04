package com.jn.langx.util.struct.counter;

import com.jn.langx.util.Preconditions;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerCounter extends IntegerCounter {
    private AtomicInteger vh;
    private int initValue;

    public AtomicIntegerCounter() {
        this(0);
    }

    public AtomicIntegerCounter(int init) {
        this.initValue = init;
        this.vh = new AtomicInteger(init);
    }

    @Override
    public Integer increment(Integer delta) {
        Preconditions.checkNotNull(delta);
        return vh.addAndGet(delta);
    }

    @Override
    public Integer getAndIncrement(Integer delta) {
        Preconditions.checkNotNull(delta);
        return vh.getAndAdd(delta);
    }

    @Override
    public Integer get() {
        return vh.get();
    }

    @Override
    public void set(Integer value) {
        Preconditions.checkNotNull(value);
        vh.set(value);
    }

    @Override
    public String toString() {
        return vh.toString();
    }

    @Override
    public void reset() {
        set(initValue);
    }
}
