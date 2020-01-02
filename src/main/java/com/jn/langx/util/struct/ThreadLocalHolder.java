package com.jn.langx.util.struct;

public class ThreadLocalHolder<V> implements ValueHolder<V> {
    private ThreadLocal<V> local;

    public ThreadLocalHolder(final V value) {
        local = new ThreadLocal<V>(){
            @Override
            protected V initialValue() {
                return value;
            }
        };
    }

    @Override
    public void set(V v) {
        local.set(v);
    }

    @Override
    public V get() {
        return local.get();
    }

    public void reset(){
        local.remove();
    }
}
