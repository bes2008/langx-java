package com.jn.langx.factory;

import com.jn.langx.Delegatable;

public class ThreadLocalFactory<I, E> implements Factory<I, E>, Delegatable<Factory<I, E>> {
    private final ThreadLocal<E> valueCache;
    private final ThreadLocal<I> inputCache = new ThreadLocal<I>();
    private Factory<I, E> delegate;

    public ThreadLocalFactory(final Factory<I, E> delegate) {
        if (delegate == null) {
            throw new NullPointerException();
        }
        setDelegate(delegate);
        this.valueCache = new ThreadLocal<E>() {
            @Override
            protected E initialValue() {
                return ThreadLocalFactory.this.delegate.create(inputCache.get());
            }
        };
    }


    @Override
    public Factory<I, E> getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(Factory<I, E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public E create(I input) {
        inputCache.set(input);
        try {
            return valueCache.get();
        } finally {
            inputCache.remove();
        }
    }

    public void clear() {
        valueCache.remove();
    }
}
