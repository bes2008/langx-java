package com.jn.langx.util.collection.iter;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;

import java.util.Enumeration;

/**
 * Wrap an Enumeration to an Iterator
 *
 * @param <E>
 */
public class EnumerationIterator<E> extends UnmodifiableIterator<E> {
    Enumeration<E> enumeration;

    public EnumerationIterator(@Nullable Enumeration<E> enumeration) {
        Preconditions.checkNotNull(enumeration);
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
        return enumeration != null && enumeration.hasMoreElements();
    }

    @Override
    public E next() {
        return enumeration != null ? enumeration.nextElement() : null;
    }
}
