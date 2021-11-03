package com.jn.langx.util.collection.sequence;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.iter.EmptyIterator;

import java.util.*;

public class EmptySequence<E> implements Sequence<E>{
    private static final EmptySequence INSTANCE = new EmptySequence();
    @Override
    public E first() {
        return null;
    }

    @Override
    public E last() {
        return null;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return EmptyIterator.INSTANCE;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return EmptyIterator.INSTANCE;
    }

    @Override
    public <S extends Sequence<E>> S subSequence(int fromIndex, int toIndex) {
        return (S)INSTANCE;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return INSTANCE;
    }

    @Override
    public List<E> asList() {
        return Collections.EMPTY_LIST;
    }
}
