package com.jn.langx.cache;

import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.timing.timer.Timer;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 先进先出队列
 * @param <K>
 * @param <V>
 */
public class FIFOCache<K, V> extends AbstractCache<K, V> {
    public FIFOCache() {
        super(Integer.MAX_VALUE, 60L * 1000);
    }

    public FIFOCache(int maxCapacity, long evictExpiredInterval) {
        super(maxCapacity, evictExpiredInterval);
    }

    public FIFOCache(int maxCapacity, long evictExpiredInterval, Timer timer) {
        super(maxCapacity, evictExpiredInterval, timer);
    }

    private LinkedHashMap<K, K> queue = new LinkedHashMap<K, K>();

    @Override
    protected void addToCache(Entry<K, V> entry) {
        K key = entry.getKey();
        if (key != null) {
            queue.put(key, key);
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
    }

    @Override
    protected void afterRead(Entry<K, V> entry) {
    }

    @Override
    protected void removeFromCache(Entry<K, V> entry, RemoveCause removeCause) {
        K key = entry.getKey();
        if (key != null) {
            queue.remove(entry.getKey());
        }
    }

    @Override
    protected List<K> forceEvict(int count) {
        return Pipeline.of(queue.keySet()).findN(count).asList();
    }
}
