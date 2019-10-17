package com.jn.langx.cache;

public interface RemoveListener<K, V> {
    void onRemove(K key, V value);
}
