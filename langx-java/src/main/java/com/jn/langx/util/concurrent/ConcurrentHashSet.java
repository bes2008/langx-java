package com.jn.langx.util.concurrent;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashSet<E> extends AbstractSet<E> {
    private ConcurrentMap<E, Integer> map;

    public ConcurrentHashSet() {
        this.map = new ConcurrentHashMap<E, Integer>();
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            return false;
        }
        map.putIfAbsent(e, 1);
        return true;
    }

    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        Integer value = map.remove(o);
        return value == null || value == 1;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        Integer v = map.get(o);
        return v != null && v == 1;
    }

    @Override
    public int size() {
        return map.size();
    }
}
