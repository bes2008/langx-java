package com.jn.langx.util.struct;

import com.jn.langx.util.Emptys;

public class ThreadLocalHolder<V> implements ValueHolder<V> {
    private ThreadLocal<V> local;

    public ThreadLocalHolder(){
        this(null);
    }

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

    @Override
    public boolean isEmpty() {
        return Emptys.isEmpty(get());
    }

    @Override
    public boolean isNull() {
        return get()==null;
    }

    @Override
    public void setHash(int hash) {
        // NOOP
    }

    @Override
    public int getHash() {
        Object v = get();
        if(v==null){
            return 0;
        }
        return v.hashCode();
    }
}
