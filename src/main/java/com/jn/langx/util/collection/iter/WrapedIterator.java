package com.jn.langx.util.collection.iter;

import com.jn.langx.util.Preconditions;

import java.util.Iterator;

public class WrapedIterator<E> implements Iterator<E> {
    private Iterator<E> delegate;
    private boolean mutable;

    public WrapedIterator(Iterator<E> delegate, boolean mutable) {
        Preconditions.checkNotNull(delegate);
        this.delegate = delegate;
        this.mutable = mutable;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public E next() {
        return delegate.next();
    }

    @Override
    public void remove() {
        if (!mutable) {
            throw new UnsupportedOperationException("Unsupported remove() on an immutable iterator");
        }
    }
}
