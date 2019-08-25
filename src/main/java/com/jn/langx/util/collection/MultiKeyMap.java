package com.jn.langx.util.collection;

import java.util.*;

public class MultiKeyMap<V> extends HashMap<Object, V> {
    private Map<Tuple, V> delegate = new HashMap<Tuple, V>();

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(new Tuple(key));
    }

    public <K2> boolean containsKey(Object key, K2 key2) {
        return delegate.containsKey(new Tuple(key, key2));
    }

    public <K2, K3> boolean containsKey(Object key, K2 key2, K3 key3) {
        return delegate.containsKey(new Tuple(key, key2, key3));
    }

    public <K2, K3, K4> boolean containsKey(Object key, K2 key2, K3 key3, K4 key4) {
        return delegate.containsKey(new Tuple(key, key2, key3, key4));
    }

    public <K2, K3, K4, K5> boolean containsKey(Object key, K2 key2, K3 key3, K4 key4, K5 key5) {
        return delegate.containsKey(new Tuple(key, key2, key3, key4, key5));
    }


    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return delegate.get(new Tuple(key));
    }

    public <K2> V get(Object key, K2 key2) {
        return delegate.get(new Tuple(key, key2));
    }

    public <K2, K3> V get(Object key, K2 key2, K3 key3) {
        return delegate.get(new Tuple(key, key2, key3));
    }

    public <K2, K3, K4> V get(Object key, K2 key2, K3 key3, K4 key4) {
        return delegate.get(new Tuple(key, key2, key3, key4));
    }

    public <K2, K3, K4, K5> V get(Object key, K2 key2, K3 key3, K4 key4, K5 key5) {
        return delegate.get(new Tuple(key, key2, key3, key4, key5));
    }

    public V put(Object key, V value) {
        return delegate.put(new Tuple(key), value);
    }

    public <K2> V put(Object key, K2 key2, V value) {
        return delegate.put(new Tuple(key, key2), value);
    }

    public <K2, K3> V put(Object key, K2 key2, K3 key3, V value) {
        return delegate.put(new Tuple(key, key2, key3), value);
    }

    public <K2, K3, K4> V put(Object key, K2 key2, K3 key3, K4 key4, V value) {
        return delegate.put(new Tuple(key, key2, key3, key4), value);
    }

    public <K2, K3, K4, K5> V put(Object key, K2 key2, K3 key3, K4 key4, K5 key5, V value) {
        return delegate.put(new Tuple(key, key2, key3, key4, key5), value);
    }


    @Override
    public V remove(Object key) {
        return delegate.remove(new Tuple(key));
    }

    public <K2> V remove2(Object key, K2 key2) {
        return delegate.remove(new Tuple(key, key2));
    }

    public <K2, K3> V remove(Object key, K2 key2, K3 key3) {
        return delegate.remove(new Tuple(key, key2, key3));
    }

    public <K2, K3, K4> V remove(Object key, K2 key2, K3 key3, K4 key4) {
        return delegate.remove(new Tuple(key, key2, key3, key4));
    }

    public <K2, K3, K4, K5> V remove(Object key, K2 key2, K3 key3, K4 key4, K5 key5) {
        return delegate.remove(new Tuple(key, key2, key3, key4, key5));
    }

    public void putAll(Map<? extends Object, ? extends V> m) {
        Iterator iter = m.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Object, V> entry = (Map.Entry<Object, V>) iter.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    @Override
    public Set entrySet() {
        return delegate.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
