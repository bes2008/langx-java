package com.jn.langx.registry;

import com.jn.langx.factory.Factory;

public interface Registry<K, V> extends Factory<K, V> {
    void register(V v);
    void register(K key, V v);
}
