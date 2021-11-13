package com.jn.langx.util.collection.forwarding;

import com.jn.langx.Delegatable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ForwardingMap<K,V> implements Map<K,V>, Delegatable<Map<K,V>> {
    private Map<K,V> delegate;

    public ForwardingMap(Map<K,V> delegate){
        setDelegate(delegate);
    }

    public ForwardingMap(){}

    @Override
    public Map<K,V> getDelegate() {
        return this.delegate;
    }

    @Override
    public void setDelegate(Map<K,V> delegate) {
        this.delegate = delegate;
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
}
