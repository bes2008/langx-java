package com.jn.langx.util.struct.counter;

public class SimpleLongCounter implements Counter<Long> {
    private long value;

    public SimpleLongCounter() {
        this.value = 0L;
    }

    public SimpleLongCounter(Long value) {
        set(value);
    }

    @Override
    public Long increment() {
        return increment(1L);
    }

    @Override
    public Long increment(Long delta) {
        value = value + delta;
        return value;
    }

    @Override
    public Long decrement() {
        return decrement(1L);
    }

    @Override
    public Long decrement(Long delta) {
        this.value = value - delta;
        return value;
    }

    @Override
    public Long get() {
        return value;
    }

    @Override
    public Long getAndIncrement() {
        return getAndIncrement(1L);
    }

    @Override
    public Long getAndIncrement(Long delta) {
        Long value = get();
        increment(delta);
        return value;
    }

    @Override
    public void set(Long value) {
        this.value = value;
    }
}
