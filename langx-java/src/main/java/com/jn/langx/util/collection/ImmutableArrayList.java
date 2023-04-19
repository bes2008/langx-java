package com.jn.langx.util.collection;

import com.jn.langx.util.collection.iter.UnmodifiableListIterator;
import com.jn.langx.util.collection.iter.WrappedIterator;

import java.util.*;

public class ImmutableArrayList<E> extends AbstractList<E> {
    private ArrayList<E> array;

    public ImmutableArrayList(E[] array){
        this.array = Collects.newArrayList(array);
    }

    public ImmutableArrayList(List<E> list){
        array = list instanceof  ArrayList ? (ArrayList<E>)list: Collects.newArrayList(list);
    }

    public ImmutableArrayList(Iterable<E> iterable){
        array = Collects.newArrayList(iterable);
    }

    @Override
    public E get(int index) {
        return array.get(index);
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        return get(index);
    }

    @Override
    public void add(int index, E element) {
        // ignore it
    }

    @Override
    public E remove(int index) {
        return get(index);
    }

    @Override
    public Iterator<E> iterator() {
        return new WrappedIterator<E>(array.iterator(), false);
    }

    @Override
    public int indexOf(Object o) {
        return array.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return array.lastIndexOf(o);
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public ListIterator<E> listIterator() {
        return array.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new UnmodifiableListIterator<E>(array.listIterator());
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return Collects.asList(super.subList(fromIndex, toIndex), false);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
