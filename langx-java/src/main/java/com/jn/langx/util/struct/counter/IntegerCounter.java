package com.jn.langx.util.struct.counter;

public abstract class IntegerCounter implements Counter<Integer>{

    @Override
    public Integer increment() {
        return increment(1);
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

}
