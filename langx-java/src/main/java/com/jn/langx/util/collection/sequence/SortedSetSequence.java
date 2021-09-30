package com.jn.langx.util.collection.sequence;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;

import java.util.*;

public class SortedSetSequence<E> implements Sequence<E> {
    private SortedSet<E> set;

    public SortedSetSequence(SortedSet<E> set) {
        this.set = set;
    }

    @Override
    public E first() {
        return set.first();
    }

    @Override
    public E last() {
        return set.last();
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return Objs.isEmpty(set);
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return set.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return set.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return set.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public E get(int index) {
        return Collects.asList(set).get(index);
    }

    @Override
    public E set(int index, E element) {
        E old = remove(index);
        add(element);
        return old;
    }

    @Override
    public void add(int index, E element) {
        add(element);
    }

    @Override
    public E remove(int index) {
        E old = get(index);
        if (old != null) {
            set.remove(old);
        }
        return old;
    }

    @Override
    public int indexOf(Object o) {
        return Collects.indexOf(Collects.asList(set), (E) o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return Collects.lastIndexOf(Collects.asList(set), (E) o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return Collects.asList(set).listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return Collects.asList(set).listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        int[] validIndexes = Arrs.toPositiveIndexes(size(), fromIndex, toIndex);
        return Collects.asList(set).subList(validIndexes[0], validIndexes[1]);
    }

    @Override
    public SortedSetSequence<E> subSequence(int fromIndex, int toIndex) {
        List<E> list = subList(fromIndex, toIndex);
        TreeSet<E> set = new TreeSet<E>(this.set.comparator());
        set.addAll(list);
        return new SortedSetSequence<E>(set);
    }

    @Override
    public List<E> asList() {
        return Collects.newArrayList(set);
    }

    @Override
    public String toString() {
        return Strings.join(",","{","}",false,this.set.iterator());
    }
}
