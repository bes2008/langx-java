package com.jn.langx.util.collection.iter;

import java.util.Iterator;

public class NullIterator<E> implements Iterator<E> {
    public static final NullIterator INSTANCE = new NullIterator();

    public NullIterator(){
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public E next() {
        return null;
    }

    @Override
    public void remove() {

    }
}