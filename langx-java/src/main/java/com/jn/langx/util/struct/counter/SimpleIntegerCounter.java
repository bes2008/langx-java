package com.jn.langx.util.struct.counter;

public class SimpleIntegerCounter implements Counter<Integer> {
    int value = 0;

    public SimpleIntegerCounter() {
        this.value = 0;
    }

    public SimpleIntegerCounter(Integer value) {
        set(value);
    }

    @Override
    public Integer increment() {
        return increment(1);
    }

    @Override
    public Integer increment(Integer delta) {
        value = value + delta;
        return value;
    }

    @Override
    public Integer decrement() {
        return decrement(1);
    }

    @Override
    public Integer decrement(Integer delta) {
        value = value - delta;
        return value;
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
    }
}
