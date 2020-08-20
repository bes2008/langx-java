package com.jn.langx.cache;

import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.timing.timer.Timer;

import java.util.LinkedHashMap;
import java.util.List;

public class LRUCache<K, V> extends AbstractCache<K, V> {

    /**
     * key: entry.key
     * Value: entry
     */
    private LinkedHashMap<K, K> lru = new LinkedHashMap<K, K>();

    public LRUCache() {
        super(Integer.MAX_VALUE, 60 * 1000);
    }

    public LRUCache(int maxCapacity, long evictExpiredInterval) {
        super(maxCapacity, evictExpiredInterval);
    }

    public LRUCache(int maxCapacity, long evictExpiredInterval, Timer timer) {
        super(maxCapacity, evictExpiredInterval, timer);
    }


    @Override
    protected void addToCache(Entry<K, V> entry) {
        K key = entry.getKey();
        lru.put(key, key);
    }

    @Override
    protected void removeFromCache(Entry<K, V> entry, RemoveCause removeCause) {
        K key = entry.getKey();
        if (key != null) {
            lru.remove(key);
        }
    }

    @Override
    protected void beforeRecomputeExpireTimeOnRead(Entry<K, V> entry) {
    }

    @Override
    protected void afterRecomputeExpireTimeOnRead(Entry<K, V> entry) {
    }

    @Override
    protected void beforeRead(Entry<K, V> entry) {
        removeFromCache(entry, null);
    }

    @Override
    protected void afterRead(Entry<K, V> entry) {
        addToCache(entry);
    }

    @Override
    protected List<K> forceEvict(final int count) {
        return Pipeline.of(lru.keySet()).findN(count).asList();
    }
}
