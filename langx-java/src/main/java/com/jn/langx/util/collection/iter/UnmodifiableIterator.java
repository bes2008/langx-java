package com.jn.langx.util.collection.iter;

import java.util.Iterator;

public abstract class UnmodifiableIterator<E> implements Iterator<E> {
    /** Constructor for use by subclasses. */
    protected UnmodifiableIterator() {}

    /**
     * Guaranteed to throw an exception and leave the underlying data unmodified.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}