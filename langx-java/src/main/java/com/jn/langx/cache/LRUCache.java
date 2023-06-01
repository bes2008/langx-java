package com.jn.langx.cache;

import com.jn.langx.util.Maths;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.concurrent.clhm.ConcurrentLinkedHashMap;
import com.jn.langx.util.os.Platform;
import com.jn.langx.util.timing.timer.Timer;

import java.util.List;

/**
 * 最近最少使用
 *
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends AbstractCache<K, V> {

    /**
     * key: entry.key
     * Value: entry
     */
    private ConcurrentLinkedHashMap<K, K> lru = new ConcurrentLinkedHashMap.Builder()
            .initialCapacity(16)
            .concurrencyLevel(Maths.min(4, Platform.cpuCore()))
            .build();

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
