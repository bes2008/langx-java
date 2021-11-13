package com.jn.langx.util.collection.forwarding;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class ForwardingList<E> extends ForwardingCollection<E, List<E>> implements List<E> {

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return getDelegate().addAll(index,c);
    }

    @Override
    public E get(int index) {
        return getDelegate().get(index);
    }

    @Override
    public E set(int index, E element) {
        return getDelegate().set(index, element);
    }

    @Override
    public void add(int index, E element) {
        getDelegate().add(index, element);
    }

    @Override
    public E remove(int index) {
        return getDelegate().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return getDelegate().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return getDelegate().lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return getDelegate().listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return getDelegate().listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return getDelegate().subList(fromIndex, toIndex);
    }
}
