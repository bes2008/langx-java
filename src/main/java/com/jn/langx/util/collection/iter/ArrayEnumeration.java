package com.jn.langx.util.collection.iter;

import java.util.Enumeration;

/**
 * @author jinuo.fang
 */
public class ArrayEnumeration<E> implements Enumeration<E> {
    private ArrayIterator<E> iterator;

    public ArrayEnumeration(Object array) {
        this.iterator = new ArrayIterator<E>(array);
    }

    public ArrayEnumeration(E[] array) {
        this.iterator = new ArrayIterator<E>(array);
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
