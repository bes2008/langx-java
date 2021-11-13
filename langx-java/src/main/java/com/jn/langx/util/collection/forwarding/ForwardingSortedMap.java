package com.jn.langx.util.collection.forwarding;

import com.jn.langx.Delegatable;

import java.util.*;

public class ForwardingSortedMap<K,V> implements SortedMap<K,V> ,Delegatable<SortedMap<K,V>> {
    private SortedMap<K,V> delegate;
    @Override
    public SortedMap<K, V> getDelegate() {
        return this.delegate;
    }

    @Override
    public void setDelegate(SortedMap<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Comparator<? super K> comparator() {
        return getDelegate().comparator();
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return getDelegate().subMap(fromKey, toKey);
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return getDelegate().headMap(toKey);
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return getDelegate().tailMap(fromKey);
    }

    @Override
    public K firstKey() {
        return getDelegate().firstKey();
    }

    @Override
    public K lastKey() {
        return getDelegate().lastKey();
    }

    @Override
    public Set<K> keySet() {
        return getDelegate().keySet();
    }

    @Override
    public Collection<V> values() {
        return getDelegate().values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return getDelegate().entrySet();
    }

    @Override
    public int size() {
        return getDelegate().size();
    }

    @Override
    public boolean isEmpty() {
        return getDelegate().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return getDelegate().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return getDelegate().containsValue(value);
    }

    @Override
    public V get(Object key) {
        return getDelegate().get(key);
    }

    @Override
    public V put(K key, V value) {
        return getDelegate().put(key, value);
    }

    @Override
    public V remove(Object key) {
        return getDelegate().remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        getDelegate().putAll(m);
    }

    @Override
    public void clear() {
        getDelegate().clear();
    }
}
