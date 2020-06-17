package com.jn.langx.util.collection.iter;

import com.jn.langx.util.Preconditions;

import java.util.ListIterator;

public class WrappedListIterator<E> implements ListIterator<E> {
    private ListIterator<E> delegate;
    private boolean mutable;

    public WrappedListIterator(ListIterator delegate) {
        this(delegate, !(delegate instanceof UnmodifiableIterator));
    }

    public WrappedListIterator(ListIterator delegate, boolean mutable) {
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
    public boolean hasPrevious() {
        return delegate.hasPrevious();
    }

    @Override
    public E previous() {
        return delegate.previous();
    }

    @Override
    public int nextIndex() {
        return delegate.nextIndex();
    }

    @Override
    public int previousIndex() {
        return delegate.previousIndex();
    }

    @Override
    public void remove() {
        if (mutable) {
            delegate.remove();
        }
    }

    @Override
    public void set(E e) {
        if (mutable) {
            delegate.set(e);
        }
    }

    @Override
    public void add(E e) {
        if (mutable) {
            delegate.add(e);
        }
    }
}
