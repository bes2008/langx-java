package com.jn.langx.util.struct.counter;

import com.jn.langx.util.Preconditions;

public class SimpleLongCounter extends LongCounter {
    private long value;

    public SimpleLongCounter() {
        this.value = 0L;
    }

    public SimpleLongCounter(Long value) {
        set(value);
    }


    @Override
    public Long increment(Long delta) {
        value = value + delta;
        return value;
    }

    @Override
    public Long get() {
        return value;
    }


    @Override
    public Long getAndIncrement(Long delta) {
        Long value = get();
        increment(delta);
        return value;
    }

    @Override
    public void set(Long value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
}
