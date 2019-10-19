package com.jn.langx.cache;

import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FIFOCache<K, V> extends AbstractCache<K, V> {
    public FIFOCache() {
        super(Integer.MAX_VALUE, 60 * 1000);
    }

    private LinkedHashMap<K, Entry<K, V>> queue = new LinkedHashMap<K, Entry<K, V>>();

    @Override
    protected void addToCache(Entry<K, V> entry) {
        queue.put(entry.getKey(), entry);
    }

    @Override
    protected void beforeRecomputeExpireTimeOnRead(Entry<K, V> entry) {

    }

    @Override
    protected void afterRecomputeExpireTimeOnRead(Entry<K, V> entry) {

    }

    @Override
    protected void removeFromCache(Entry<K, V> entry, RemoveCause removeCause) {
        queue.remove(entry.getKey());
    }

    @Override
    protected List<Entry<K, V>> forceEvict(int count) {
        final List<Entry<K, V>> ret = new ArrayList<Entry<K, V>>();
        Pipeline.of(new ArrayList<K>(queue.keySet())).limit(count).forEach(new Consumer<K>() {
            @Override
            public void accept(K k) {
                Entry<K, V> entry = queue.remove(k);
                if (entry != null) {
                    ret.add(entry);
                }
            }
        });

        return ret;
    }
}
