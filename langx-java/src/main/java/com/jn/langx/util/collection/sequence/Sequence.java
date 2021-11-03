package com.jn.langx.util.collection.sequence;

import com.jn.langx.util.collection.Listable;

import java.util.*;

public interface Sequence<E> extends List<E>, Listable<E> {
    E first();
    E last();

    @Override
    boolean isNull();

    @Override
    int size();

    @Override
    boolean isEmpty();

    @Override
    boolean contains(Object o);

    @Override
    Iterator<E> iterator();

    @Override
    Object[] toArray();

    @Override
    <T> T[] toArray(T[] a);

    @Override
    boolean add(E e);

    @Override
    boolean remove(Object o);

    @Override
    boolean containsAll(Collection<?> c);

    @Override
    boolean addAll(Collection<? extends E> c);

    @Override
    boolean addAll(int index, Collection<? extends E> c);

    @Override
    boolean removeAll(Collection<?> c);

    @Override
    boolean retainAll(Collection<?> c);

    @Override
    void clear();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    E get(int index);

    @Override
    E set(int index, E element);

    @Override
    void add(int index, E element);

    @Override
    E remove(int index);

    @Override
    int indexOf(Object o);

    @Override
    int lastIndexOf(Object o);

    @Override
    ListIterator<E> listIterator();

    @Override
    ListIterator<E> listIterator(int index);

    <S extends Sequence<E>> S subSequence(int fromIndex, int toIndex);

    @Override
    List<E> subList(int fromIndex, int toIndex);

    List<E> asList();

    @Override
    String toString();
}
