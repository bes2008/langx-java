package com.jn.langx.util.collection;

import com.jn.langx.util.EmptyEvalutible;

import java.util.Collection;
import java.util.Iterator;

public interface Listable<E> extends Iterable<E>, EmptyEvalutible {
    boolean add(E e);

    /**
     * 移除第一个
     * @param e
     */
    boolean remove(Object e);

    boolean removeAll(Collection<?> collection);

    void clear();

    boolean addAll(Collection<? extends E> elements);

    @Override
    Iterator<E> iterator();

    @Override
    boolean isEmpty();

    @Override
    boolean isNull();
}
