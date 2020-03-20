package com.jn.langx.util.collection.iter;

import com.jn.langx.util.Preconditions;

import java.util.Enumeration;

public class CompositeEnumeration<E> implements Enumeration<E> {
    private CompositeIterator<E> iterator;

    public CompositeEnumeration(){
        this.iterator = new CompositeIterator<E>();
    }

    public void addEnumeration(Enumeration<E> enumeration){
        iterator.add(new EnumerationIterator<E>(enumeration));
    }

    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    @Override
    public E nextElement() {
        return iterator.next();
    }
}
