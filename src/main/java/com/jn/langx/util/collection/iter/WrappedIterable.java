package com.jn.langx.util.collection.iter;

import com.jn.langx.annotation.NonNull;

import java.util.Iterator;

public class WrappedIterable<E> implements Iterable<E> {
    private Iterable<E> delegate;
    private boolean mutable;

    public WrappedIterable(@NonNull Iterable<E> delegate, boolean mutable) {
        this.delegate = delegate;
        this.mutable = mutable;
    }

    @Override
    public Iterator<E> iterator() {
        return new WrappedIterator<E>(delegate.iterator(), mutable);
    }

    public boolean isMutable() {
        return mutable;
    }
}
