package com.jn.langx.registry;

import com.jn.langx.Factory;

public interface Registry<K, V> extends Factory<K, V> {
    void register(V v);

    void register(K key, V v);

    /**
     * @param key the key
     * @since 4.4.2
     */
    void unregister(K key);

    /**
     * @since 4.4.2
     */
    boolean contains(K key);
}
