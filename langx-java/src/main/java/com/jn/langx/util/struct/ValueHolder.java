package com.jn.langx.util.struct;

public interface ValueHolder<V> extends Reference<V>{
    void set(V v);
    void reset();
    boolean isEmpty();
}
