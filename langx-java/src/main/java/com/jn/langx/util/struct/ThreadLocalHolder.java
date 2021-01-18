package com.jn.langx.util.struct;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.function.Supplier0;

public class ThreadLocalHolder<V> implements ValueHolder<V> {
    private ThreadLocal<V> local;

    public ThreadLocalHolder() {
        this((V) null);
    }

    public ThreadLocalHolder(final Supplier0<V> supplier) {
        this.local = new ThreadLocal<V>() {
            @Override
            protected V initialValue() {
                return supplier == null ? null : supplier.get();
            }
        };
    }

    public ThreadLocalHolder(final V value) {
        this(new Supplier0<V>() {
            @Override
            public V get() {
                return value;
            }
        });
    }

    @Override
    public void set(V v) {
        local.set(v);
    }

    @Override
    public V get() {
        return local.get();
    }

    public void reset() {
        local.remove();
    }

    @Override
    public boolean isEmpty() {
        return Emptys.isEmpty(get());
    }

    @Override
    public boolean isNull() {
        return get() == null;
    }

    @Override
    public void setHash(int hash) {
        // NOOP
    }

    @Override
    public int getHash() {
        Object v = get();
        if (v == null) {
            return 0;
        }
        return v.hashCode();
    }
}
