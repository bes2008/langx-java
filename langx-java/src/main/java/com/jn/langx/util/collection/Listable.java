package com.jn.langx.util.collection;

import com.jn.langx.util.EmptyEvalutible;

import java.util.Collection;
import java.util.Iterator;

public interface Listable<E> extends Iterable<E>, EmptyEvalutible {
    void add(E e);

    void remove(E e);

    void clear(E e);

    void addAll(Collection<E> elements);

    @Override
    Iterator<E> iterator();

    @Override
    boolean isEmpty();

    @Override
    boolean isNull();
}
