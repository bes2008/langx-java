package com.jn.langx.util.struct.counter;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongCounter implements Counter<Long> {
    private AtomicLong vh;

    public AtomicLongCounter() {
        this(0);
    }

    public AtomicLongCounter(long init) {
        this.vh = new AtomicLong(init);
    }


    @Override
    public Long increment() {
        return increment(1L);
    }

    @Override
    public Long increment(Long delta) {
        return vh.addAndGet(delta);
    }

    @Override
    public Long decrement() {
        return decrement(1L);
    }

    @Override
    public Long decrement(Long delta) {
        return increment(-delta);
    }

    @Override
    public Long getAndIncrement() {
        return getAndIncrement(1L);
    }

    @Override
    public Long getAndIncrement(Long delta) {
        return vh.getAndAdd(delta);
    }

    @Override
    public Long get() {
        return vh.get();
    }

    @Override
    public void set(Long value) {
        vh.set(value);
    }
}
