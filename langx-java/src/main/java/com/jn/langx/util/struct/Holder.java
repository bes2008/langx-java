package com.jn.langx.util.struct;

import com.jn.langx.util.Emptys;

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

    @Override
    public boolean isNull() {
        return v == null;
    }

    @Override
    public boolean isEmpty() {
        return Emptys.isEmpty(v);
    }

    @Override
    public void setHash(int hash) {
    }

    @Override
    public int getHash() {
        return isNull() ? 0 : v.hashCode();
    }
}
