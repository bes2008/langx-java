package com.jn.langx.util.struct;

public class Holder<V> implements ValueHolder<V> {
    private V v;
    private final V initValue;

    public Holder() {
        this.initValue = null;
    }

    public Holder(final V value) {
        this.initValue = value;
        this.set(value);
    }

    public void set(final V value) {
        this.v = value;
    }

    public V get() {
        return this.v;
    }

    @Override
    public void reset() {
        this.v = initValue;
    }
}
