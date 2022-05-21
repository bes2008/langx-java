package com.jn.langx.util.struct.counter;

import com.jn.langx.util.Preconditions;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongCounter extends LongCounter {
    private AtomicLong vh;

    public AtomicLongCounter() {
        this(0);
    }

    public AtomicLongCounter(long init) {
        this.vh = new AtomicLong(init);
    }

    @Override
    public Long increment(Long delta) {
        Preconditions.checkNotNull(delta);
        return vh.addAndGet(delta);
    }


    @Override
    public Long getAndIncrement(Long delta) {
        Preconditions.checkNotNull(delta);
        return vh.getAndAdd(delta);
    }

    @Override
    public Long get() {
        return vh.get();
    }

    @Override
    public void set(Long value) {
        Preconditions.checkNotNull(value);
        vh.set(value);
    }
}
