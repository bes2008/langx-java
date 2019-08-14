package com.jn.langx.util.collection;

import java.util.Iterator;

public class WrapedIterator<E> implements Iterator<E> {
    private Iterator<E> delegate;
    private boolean mutable;

    public WrapedIterator(Iterator<E> delegate, boolean mutable) {
        this.delegate = delegate;
        this.mutable = mutable;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public E next() {
        return null;
    }

    @Override
    public void remove() {
        if (!mutable) {
            throw new UnsupportedOperationException("Unsupported remove() on an immutable iterator");
        }
    }
}
