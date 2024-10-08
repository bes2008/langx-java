package com.jn.langx.util.concurrent;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class CopyOnWriteConcurrentMap<K,V> implements ConcurrentMap<K, V> {

    private volatile Map<K, V> delegate = Collections.emptyMap();

    public CopyOnWriteConcurrentMap() {
    }

    public CopyOnWriteConcurrentMap(Map<K, V> existing) {
        this.delegate = new HashMap<K,V>(existing);
    }

    @Override
    public synchronized V putIfAbsent(K key, V value) {
        final Map<K, V> delegate = this.delegate;
        V existing = delegate.get(key);
        if(existing != null) {
            return existing;
        }
        putInternal(key, value);
        return null;
    }

    @Override
    public synchronized boolean remove(Object key, Object value) {
        final Map<K, V> delegate = this.delegate;
        V existing = delegate.get(key);
        if(existing.equals(value)) {
            removeInternal(key);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean replace(K key, V oldValue, V newValue) {
        final Map<K, V> delegate = this.delegate;
        V existing = delegate.get(key);
        if(existing.equals(oldValue)) {
            putInternal(key, newValue);
            return true;
        }
        return false;
    }

    @Override
    public synchronized V replace(K key, V value) {
        final Map<K, V> delegate = this.delegate;
        V existing = delegate.get(key);
        if(existing != null) {
            putInternal(key, value);
            return existing;
        }
        return null;
    }

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
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return delegate.get(key);
    }

    @Override
    public synchronized V put(K key, V value) {
        return putInternal(key, value);
    }

    @Override
    public synchronized V remove(Object key) {
        return removeInternal(key);
    }

    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        final Map<K, V> delegate = new HashMap<K,V>(this.delegate);
        delegate.putAll(m);
        this.delegate = delegate;
    }

    @Override
    public synchronized void clear() {
        delegate = Collections.emptyMap();
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

    //must be called under lock
    private V putInternal(final K key, final V value) {
        final Map<K, V> delegate = new HashMap<K,V>(this.delegate);
        V existing = delegate.put(key, value);
        this.delegate = delegate;
        return existing;
    }

    public V removeInternal(final Object key) {
        final Map<K, V> delegate = new HashMap<K,V>(this.delegate);
        V existing = delegate.remove(key);
        this.delegate = delegate;
        return existing;
    }
}