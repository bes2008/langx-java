package com.jn.langx.cache;

import java.util.concurrent.TimeUnit;

public interface Cache<K, V> {
    void set(K key, V value);

    void set(K key, V value, int expire, TimeUnit timeUnit);

    V get(K key);

    int size();
}
