package com.jn.langx.util.struct;

public interface ValueHolder<V> {
    void set(V v);
    V get();
    void reset();
}
