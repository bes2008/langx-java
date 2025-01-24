package com.jn.langx.util.collection.list;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.BoundedCollection;
import com.jn.langx.util.collection.forwarding.ForwardingList;

import java.util.List;

public class BoundedList<E> extends ForwardingList<E> implements BoundedCollection<E> {
    private int maxSize;

    public BoundedList(List<E> list, int maxSize) {
        setDelegate(list);
        Preconditions.checkArgument(maxSize >= 0);
        this.maxSize = maxSize;
    }

    @Override
    public boolean isFull() {
        return size() >= maxSize;
    }

    @Override
    public boolean add(E e) {
        if(!isFull()) {
            return super.add(e);
        }
        return false;
    }

    @Override
    public void add(int index, E element) {
        if(!isFull()) {
            super.add(index, element);
        }
    }

    @Override
    public int maxSize() {
        return this.maxSize;
    }
}
