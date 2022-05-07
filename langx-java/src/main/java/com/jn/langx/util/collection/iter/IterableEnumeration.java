package com.jn.langx.util.collection.iter;

import com.jn.langx.annotation.Nullable;

import java.util.Enumeration;

public class IterableEnumeration<E> implements Enumeration<E> {
    private IteratorEnumeration<E> delegate;

    public IterableEnumeration(@Nullable Iterable<E> iterable) {
        this(iterable == null ? null : new IteratorEnumeration<E>(iterable.iterator()));
    }


    public IterableEnumeration(@Nullable IteratorEnumeration<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasMoreElements() {
        return delegate != null && delegate.hasMoreElements();
    }

    @Override
    public E nextElement() {
        return delegate != null ? delegate.nextElement() : null;
    }
}
