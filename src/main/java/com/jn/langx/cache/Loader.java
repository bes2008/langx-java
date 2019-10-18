package com.jn.langx.cache;

import java.util.Map;

public interface Loader<K, V> {
    V load(K key);

    Map<K, V> getAll(Iterable<K> keys);
}
