package com.jn.langx.util.struct.counter;

import com.jn.langx.util.Preconditions;

public class ThreadLocalIntegerCounter extends IntegerCounter {
    private ThreadLocal<Integer> valueHolder;
    private int initValue;

    public ThreadLocalIntegerCounter() {
        this(0);
    }

    public ThreadLocalIntegerCounter(Integer value) {
        Preconditions.checkNotNull(value);
        this.initValue=value;
        this.valueHolder = new ThreadLocal<Integer>(){
            @Override
            protected Integer initialValue() {
                return initValue;
            }
        };
    }

    @Override
    public Integer increment(Integer delta) {
        Preconditions.checkNotNull(delta);
        int ret = valueHolder.get() + delta;
        this.valueHolder.set(ret);
        return ret;
    }

    @Override
    public Integer getAndIncrement(Integer delta) {
        Preconditions.checkNotNull(delta);
        int ret = valueHolder.get();
        this.valueHolder.set(ret + delta);
        return ret;
    }

    @Override
    public Integer get() {
        return this.valueHolder.get();
    }

    @Override
    public void set(Integer value) {
        Preconditions.checkNotNull(value);
        this.valueHolder.set(value);
    }

    @Override
    public void reset() {
        this.valueHolder.remove();
    }

    @Override
    public String toString() {
        return get().toString();
    }
}
