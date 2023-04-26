package com.jn.langx.util.collection.iter;

import com.jn.langx.util.Objs;

import java.util.Iterator;
import java.util.List;

public class CircularIterator<T> implements Iterator<T> {
    int i = 0;
    private List<T> list;

    public CircularIterator(List<T> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return Objs.isNotEmpty(list);
    }

    @Override
    public T next() {
        T next = list.get(i);
        i = (i + 1) % list.size();
        return next;
    }

    public T peek() {
        return list.get(i);
    }

    @Override
    public void remove() {
        if (i >= 0 && i < list.size()) {
            list.remove(i);
            i--;
            if (i < 0) {
                i = 0;
            }
        }
    }
}
