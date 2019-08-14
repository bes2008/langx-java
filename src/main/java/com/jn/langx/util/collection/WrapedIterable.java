package com.jn.langx.util.collection;

import java.util.Iterator;

public class WrapedIterable<E> implements Iterable<E> {
    private Iterable<E> delegate;
    private boolean mutable;

    public WrapedIterable(Iterable<E> delegate, boolean mutable) {
        this.delegate = delegate;
        this.mutable = mutable;
    }

    @Override
    public Iterator<E> iterator() {
        return new WrapedIterator<E>(delegate.iterator(), mutable);
    }

    public boolean isMutable() {
        return mutable;
    }
}
