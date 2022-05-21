package com.jn.langx.util.struct.counter;

import com.jn.langx.util.Preconditions;

public class SimpleIntegerCounter extends IntegerCounter {
    int value = 0;

    public SimpleIntegerCounter() {
        this.value = 0;
    }

    public SimpleIntegerCounter(Integer value) {
        set(value);
    }

    @Override
    public Integer increment(Integer delta) {
        value = value + delta;
        return value;
    }


    @Override
    public Integer get() {
        return value;
    }

    @Override
    public Integer getAndIncrement(Integer delta) {
        Integer value = get();
        increment(delta);
        return value;
    }

    @Override
    public void set(Integer value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
}
