package com.jn.langx.util.collection.iter;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

import java.util.Iterator;

/**
 * Wrap an Iterator to an Iterable
 *
 * @param <E>
 */
public class IteratorIterable<E> implements Iterable<E> {
    private Iterator<E> iterator;

    public IteratorIterable(@NonNull Iterator<E> iterator) {
        this(iterator, true);
    }

    public IteratorIterable(@NonNull Iterator<E> iterator, boolean removable) {
        Preconditions.checkNotNull(iterator);
        if (removable) {
            this.iterator = iterator;
        } else {
            this.iterator = new UnremoveableIterator<E>(iterator);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return iterator;
    }

    private static class UnremoveableIterator<E> implements Iterator<E> {
        private Iterator<E> delegate;

        private UnremoveableIterator(Iterator<E> delegate) {
            this.delegate = delegate;
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
            throw new UnsupportedOperationException("Can't remove element when using an unremovable iterator");
        }
    }
}
