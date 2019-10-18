package com.jn.langx.cache;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LRUCache<K, V> extends AbstractCache<K, V> {

    /**
     * Key: expire time
     * Value: key
     */
    private Map<Long, List<K>> index = WrappedNonAbsentMap.wrap(new TreeMap<Long, List<K>>(new ComparableComparator<Long>()), new Supplier<Long, List<K>>() {
        @Override
        public List<K> get(Long expireTime) {
            return Collects.emptyLinkedList();
        }
    });


    public LRUCache() {
        super(Integer.MAX_VALUE, 60 * 1000);
    }

    public LRUCache(int maxCapatity, long evictExpiredInterval) {
        super(maxCapatity, evictExpiredInterval);
    }

    @Override
    protected void addToCache(Entry<K, V> entry) {
        index.get(entry.getExpireTime()).add(entry.getKey());
    }

    @Override
    protected void removeFromCache(Entry<K, V> entry) {
        index.get(entry.getExpireTime()).remove(entry.getKey());
    }

    @Override
    protected void onRead(Entry<K, V> entry, long oldExpireTime) {
        index.get(oldExpireTime).add(entry.getKey());
        addToCache(entry);
    }

    @Override
    protected void clearExpired() {
        long now = System.currentTimeMillis();
        List<Long> expireTimes = new ArrayList<Long>(index.keySet());
        for (Long expireTime : expireTimes) {
            if (expireTime > now) {
                break;
            }
            List<K> keys = index.get(expireTime);
            Collects.forEach(keys, new Consumer2<Integer, K>() {
                @Override
                public void accept(Integer expireTime, K key) {
                    remove(key, RemoveCause.COLLECTED);
                }
            });
        }
    }

}
