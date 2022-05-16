package com.jn.langx.util.collection;

import java.util.HashMap;
import java.util.Map;

public class OneToOneMap<K, V> extends HashMap<K, V> {
    public OneToOneMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public OneToOneMap(int initialCapacity) {
        super(initialCapacity);
    }

    public OneToOneMap() {
    }

    public OneToOneMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    @Override
    public V put(K key, V value) {
        if (!containsKey(key) && !containsValue(value)) {
            super.put(key, value);
        }
        return null;
    }

    public V replace(K key, V value) {
        if (containsKey(key)) {
            V oldValue = get(key);
            if (!containsValue(value)) {
                put(key, value);
            }
            return oldValue;
        }
        return null;
    }

}
