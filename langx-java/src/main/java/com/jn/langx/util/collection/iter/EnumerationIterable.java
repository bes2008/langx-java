package com.jn.langx.util.collection.iter;

import com.jn.langx.annotation.Nullable;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Wrap an Enumeration or an EnumerationIterator to Iterable
 *
 * @param <E>
 */
public class EnumerationIterable<E> implements Iterable<E> {
    private EnumerationIterator<E> iterator;

    public EnumerationIterable(@Nullable Enumeration<E> enumeration) {
        this(new EnumerationIterator<E>(enumeration));
    }

    public EnumerationIterable(@Nullable EnumerationIterator<E> iterator) {
        this.iterator = iterator == null ? new EnumerationIterator<E>(null) : iterator;
    }

    @Override
    public Iterator<E> iterator() {
        return iterator;
    }
}
