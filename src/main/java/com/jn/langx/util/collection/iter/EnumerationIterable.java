package com.jn.langx.util.collection.iter;

import com.jn.langx.annotation.NonNull;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Wrap an Enumeration or an EnumerationIterator to Iterable
 * @param <E>
 */
public class EnumerationIterable<E> implements Iterable<E> {
    private EnumerationIterator<E> iterator;

    public EnumerationIterable(@NonNull Enumeration<E> enumeration) {
        this(new EnumerationIterator<E>(enumeration));
    }

    public EnumerationIterable(@NonNull EnumerationIterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Iterator<E> iterator() {
        return iterator;
    }
}
