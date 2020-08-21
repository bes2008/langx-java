package com.jn.langx.cache;

import com.jn.langx.annotation.Unreleased;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.timing.timer.Timer;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * 使用频度最少的
 * Least Frequently Used
 */
@Unreleased
class LFUCache <K, V> extends AbstractCache<K, V> {
    public LFUCache() {
        super(Integer.MAX_VALUE, 60 * 1000);
    }

    public LFUCache(int maxCapacity, long evictExpiredInterval) {
        super(maxCapacity, evictExpiredInterval);
    }

    public LFUCache(int maxCapacity, long evictExpiredInterval, Timer timer) {
        super(maxCapacity, evictExpiredInterval, timer);
    }

    // 这里应该是一个能根据值排序的集合，目前不满足需要重写
    private TreeMap<K, Long> lastUsedTimeSortedMap = new TreeMap<K, Long>(new Comparator<K>() {
        @Override
        public int compare(K o1, K o2) {
            return 0;
        }
    });

    @Override
    protected void addToCache(Entry<K, V> entry) {
        K key = entry.getKey();
        if (key != null) {
            lastUsedTimeSortedMap.put(key, entry.getLastUsedTime());
        }
    }

    @Override
    protected void removeFromCache(Entry<K, V> entry, RemoveCause removeCause) {
        K key = entry.getKey();
        if (key != null) {
            lastUsedTimeSortedMap.remove(entry.getKey());
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
    protected List<K> forceEvict(int count) {
        return Pipeline.of(lastUsedTimeSortedMap.keySet()).findN(count).asList();
    }
}
