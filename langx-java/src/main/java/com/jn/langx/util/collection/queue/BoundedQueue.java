package com.jn.langx.util.collection.queue;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.BoundedCollection;
import com.jn.langx.util.collection.forwarding.ForwardingList;

import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;


public class BoundedQueue<E> extends ForwardingList<E> implements Queue<E>, BoundedCollection<E> {
    private int maxSize;
    public BoundedQueue(int maxCapacity){
        setDelegate(new LinkedList<E>());
        Preconditions.checkArgument(maxCapacity>=0);
        this.maxSize = maxCapacity;
    }
    @Override
    public boolean isFull() {
        return size()>=maxSize;
    }

    @Override
    public int maxSize() {
        return maxSize;
    }

    @Override
    public boolean add(E e) {
        if(e == null){
            throw new NullPointerException();
        }
        if(!isFull()) {
            return super.add(e);
        }
        return false;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return super.remove(0);
    }

    @Override
    public E poll() {
        if(isEmpty()){
            return null;
        }
        return super.remove(0);
    }

    @Override
    public E element() {
        if(isEmpty()) {
            return null;
        }
        return getDelegate().get(0);
    }

    @Override
    public E peek() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return getDelegate().get(0);
    }
}
