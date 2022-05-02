package com.jn.langx.util.struct;

public interface ValueHolder<V> extends Ref<V>{
    void set(V v);
    void reset();
    @Override
    V get();
}
