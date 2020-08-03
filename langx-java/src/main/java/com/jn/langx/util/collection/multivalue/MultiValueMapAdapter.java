package com.jn.langx.util.collection.multivalue;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Supplier;

import java.io.Serializable;
import java.util.*;

public class MultiValueMapAdapter<K, V> implements MultiValueMap<K, V>, Serializable {

    private final Map<K, List<V>> map;

    public MultiValueMapAdapter(Map<K, List<V>> map) {
        Preconditions.checkNotNull(map, "'map' must not be null");
        this.map = map;
    }

    @Override
    @Nullable
    public V getFirst(K key) {
        List<V> values = this.map.get(key);
        return (values != null ? values.get(0) : null);
    }

    @Override
    public void add(K key, @Nullable V value) {
        List<V> values = Maps.putIfAbsent(this.map, key, new Supplier<K, List<V>>() {
            @Override
            public List<V> get(K input) {
                return new LinkedList<V>();
            }
        });
        values.add(value);
    }

    @Override
    public void addAll(K key, List<? extends V> values) {
        List<V> currentValues = Maps.putIfAbsent(this.map, key, new Supplier<K, List<V>>() {
            @Override
            public List<V> get(K input) {
                return new LinkedList<V>();
            }
        });
        currentValues.addAll(values);
    }

    @Override
    public void addIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            add(key, value);
        }
    }

    @Override
    public void addAll(MultiValueMap<K, V> values) {
        for (Entry<K, List<V>> entry : values.entrySet()) {
            addAll(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void set(K key, @Nullable V value) {
        List<V> values = new LinkedList<V>();
        values.add(value);
        this.map.put(key, values);
    }

    @Override
    public void setAll(Map<K, V> values) {
        Collects.forEach(values, new Consumer2<K, V>() {
            @Override
            public void accept(K key, V value) {
                set(key, value);
            }
        });
    }

    @Override
    public Map<K, V> toSingleValueMap() {
        final LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K, V>(this.map.size());
        Collects.forEach(map, new Consumer2<K, List<V>>() {
            @Override
            public void accept(K key, List<V> value) {
                singleValueMap.put(key, value.get(0));
            }
        });
        return singleValueMap;
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public List<V> get(Object key) {
        return this.map.get(key);
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return this.map.put(key, value);
    }

    @Override
    public List<V> remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> map) {
        this.map.putAll(map);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<List<V>> values() {
        return this.map.values();
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        return this.map.equals(other);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }
}