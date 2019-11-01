package com.jn.langx.cache;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCacheLoader<K, V> implements Loader<K, V> {
    @Override
    public Map<K, V> getAll(Iterable<K> keys) {
        final Map<K, V> map = new HashMap<K, V>();
        Collects.forEach(keys, new Consumer<K>() {
            @Override
            public void accept(K k) {
                map.put(k, load(k));
            }
        });
        return map;
    }
}
