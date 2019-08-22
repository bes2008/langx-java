package com.jn.langx.util.collection.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author jinuo.fang
 */
public class ArrayIterator<E> implements Iterable<E>, Iterator<E> {
    private final E[] array;
    private int index = 0;
    private final int length;

    public ArrayIterator(E[] array) {
        this.array = array;
        this.length = array == null ? 0 : array.length;
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return index < length;
    }

    @Override
    public E next() {
        if (hasNext()) {
            return array[index++];
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
