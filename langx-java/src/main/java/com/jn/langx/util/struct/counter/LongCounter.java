package com.jn.langx.util.struct.counter;

public abstract class LongCounter implements Counter<Long>{

    @Override
    public Long increment() {
        return increment(1L);
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

}
