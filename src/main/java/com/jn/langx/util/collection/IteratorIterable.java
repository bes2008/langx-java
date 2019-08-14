package com.jn.langx.util.collection;

import java.util.Iterator;

/**
 * Wrap an Iterator to an Iterable
 * @param <E>
 */
public class IteratorIterable<E> implements Iterable<E>{
    private Iterator<E> iterator;
    public IteratorIterable(Iterator<E> iterator){
        this.iterator = iterator;
    }

    @Override
    public Iterator<E> iterator() {
        return iterator;
    }
}
