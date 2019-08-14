package com.jn.langx.util.collection;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Wrap an Enumeration to an Iterator
 * @param <E>
 */
public class EnumerationIterator<E> implements Iterator<E> {
    Enumeration<E> enumeration;

    public EnumerationIterator(Enumeration<E> enumeration){
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    @Override
    public E next() {
        return enumeration.nextElement();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove operation is unsupported in an enumeration iterator");
    }
}
