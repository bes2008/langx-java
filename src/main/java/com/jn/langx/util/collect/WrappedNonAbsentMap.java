package com.jn.langx.util.collect;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collect.function.Suppller;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class WrappedNonAbsentMap<K, V> implements Map<K, V> {
    private Map<K, V> delegate;
    private Suppller<K, V> suppller;

    public WrappedNonAbsentMap(Map<K, V> map, Suppller<K, V> suppller) {
        this.delegate = map;
        this.suppller = suppller;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return Emptys.isEmpty(delegate);
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        K key0 = (K) key;
        V v = getIfPresent(key0);
        if (v == null) {
            return putIfAbsent(key0, suppller.get(key0));
        }
        return v;
    }

    public V getIfPresent(Object key) {
        return delegate.get(key);
    }

    public V putIfAbsent(K key, V value) {
        V v = delegate.get(key);
        if (v == null) {
            return delegate.put(key, value);
        }
        return v;
    }

    @Override
    public V put(K key, V value) {
        return delegate.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (Emptys.isNotEmpty(m)) {
            delegate.putAll(m);
        }
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegate.entrySet();
    }
}
