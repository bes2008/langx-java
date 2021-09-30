package com.jn.langx.util.collection.sequence;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Tuple;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TupleSequence implements Sequence<Object> {
    private Tuple tuple;

    public TupleSequence(Tuple tuple) {
        this.tuple = tuple;
    }

    @Override
    public Object first() {
        return tuple.get(0);
    }

    @Override
    public Object last() {
        return tuple.get(size() - 1);
    }

    @Override
    public boolean isNull() {
        return tuple == null;
    }

    @Override
    public int size() {
        return tuple.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return tuple.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return tuple.iterator();
    }

    @Override
    public Object[] toArray() {
        return tuple.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return tuple.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        if (size() < tuple.capacity()) {
            tuple.getTarget().add(o);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<?> c) {
        if (size() + c.size() <= tuple.capacity()) {
            return tuple.getTarget().addAll(c);
        }
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        if (size() + c.size() <= tuple.capacity()) {
            return tuple.getTarget().addAll(index, c);
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return tuple.getTarget().containsAll(c);
    }

    @Override
    public Object get(int index) {
        return tuple.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return tuple.getTarget().set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        if (size() < tuple.capacity()) {
            tuple.getTarget().add(index, element);
        }
    }

    @Override
    public Object remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        return tuple.getTarget().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return tuple.getTarget().lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return tuple.getTarget().listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return tuple.getTarget().listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        int[] validIndexes = Arrs.toPositiveIndexes(size(), fromIndex, toIndex);
        return tuple.getTarget().subList(validIndexes[0], validIndexes[1]);
    }

    @Override
    public TupleSequence subSequence(int fromIndex, int toIndex) {
        return new TupleSequence(new Tuple(subList(fromIndex, toIndex)));
    }

    @Override
    public List<Object> asList() {
        return Collects.newArrayList(tuple.getTarget());
    }

    @Override
    public String toString() {
        return this.tuple.toString();
    }
}
