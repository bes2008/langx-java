package com.jn.langx.util.collection.forwarding;

import java.util.Comparator;
import java.util.SortedSet;

public class ForwardingSortedSet<E> extends ForwardingCollection<E,SortedSet<E>> implements SortedSet<E> {
    @Override
    public Comparator<? super E> comparator() {
        return getDelegate().comparator();
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return getDelegate().subSet(fromElement, toElement);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return getDelegate().headSet(toElement);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return getDelegate().tailSet(fromElement);
    }

    @Override
    public E first() {
        return getDelegate().first();
    }

    @Override
    public E last() {
        return getDelegate().last();
    }
}
