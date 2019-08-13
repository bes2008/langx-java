package com.jn.langx.util.collect;

import com.jn.langx.util.collect.function.Suppller;

import java.util.HashMap;
import java.util.Map;

public class NonAbsentHashMap<K, V> extends HashMap<K, V> {
    private Suppller<K, V> suppller;

    public NonAbsentHashMap(Suppller<K, V> suppller) {
        super();
        this.suppller = suppller;
    }

    public NonAbsentHashMap(int initialCapacity, Suppller<K, V> suppller) {
        this(initialCapacity, initialCapacity, suppller);
    }

    public NonAbsentHashMap(int initialCapacity, float loadFactor, Suppller<K, V> suppller) {
        super(initialCapacity, loadFactor);
        this.suppller = suppller;
    }

    public NonAbsentHashMap(Map<? extends K, ? extends V> m, Suppller<K, V> suppller) {
        super(m);
        this.suppller = suppller;
    }

    @Override
    public V get(Object key) {
        V v = getIfPresent(key);
        if (v == null) {
            v = putIfAbsent((K) key, suppller.get((K) key));
        }
        return v;
    }

    public V getIfPresent(Object key) {
        return super.get(key);
    }

    public V putIfAbsent(K key, V value) {
        V v = super.get(key);
        if (v == null) {
            return super.put(key, value);
        }
        return v;
    }
}
