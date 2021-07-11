package com.jn.langx.util.collection.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class NullIterator<E> implements Iterator<E> {
    public static final NullIterator INSTANCE = new NullIterator();

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public E next() {
       throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}