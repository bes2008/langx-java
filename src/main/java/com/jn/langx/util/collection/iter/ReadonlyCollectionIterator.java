package com.jn.langx.util.collection.iter;

import java.util.Iterator;

public class ReadonlyCollectionIterator<E> extends UnmodifiableIterator<E> {
    private Iterator<E> iterator;
    public ReadonlyCollectionIterator(Iterable<E> iterable){
        this.iterator = iterable.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return iterator.next();
    }
}
