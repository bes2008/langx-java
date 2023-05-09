package com.jn.langx.util.struct.counter;

import com.jn.langx.util.Preconditions;

public class ThreadLocalLongCounter extends LongCounter {
    private ThreadLocal<Long> valueHolder;

    public ThreadLocalLongCounter() {
        this(0L);
    }

    public ThreadLocalLongCounter(Long value) {
        Preconditions.checkNotNull(value);
        this.valueHolder = new ThreadLocal<Long>();
        this.valueHolder.set(value);
    }

    @Override
    public Long increment(Long delta) {
        Preconditions.checkNotNull(delta);
        Long old = this.valueHolder.get();
        Long newValue = old == null ? delta : (old + delta);
        this.valueHolder.set(newValue);
        return newValue;
    }

    @Override
    public Long getAndIncrement(Long delta) {
        Preconditions.checkNotNull(delta);
        Long old = this.valueHolder.get();
        this.valueHolder.set(old == null ? delta : (old + delta));
        return old;
    }

    @Override
    public Long get() {
        return this.valueHolder.get();
    }

    @Override
    public void set(Long value) {
        Preconditions.checkNotNull(value);
        this.valueHolder.set(value);
    }
}
