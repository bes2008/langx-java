package com.jn.langx.util.struct.counter;

import com.jn.langx.util.Preconditions;

public class SimpleLongCounter extends LongCounter {
    private long value;
    private long initValue;
    public SimpleLongCounter() {
        this(0L);
    }

    public SimpleLongCounter(Long value) {
        this.initValue=value==null?0L:value;
        set(this.initValue);
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

    @Override
    public void reset() {
        set(initValue);
    }
}
