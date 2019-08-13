package com.jn.langx.util.collect;

import java.util.Iterator;

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
