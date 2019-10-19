package com.jn.langx.cache;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Supplier;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();


    public LRUCache() {
        super(Integer.MAX_VALUE, 60 * 1000);
    }

    public LRUCache(int maxCapatity, long evictExpiredInterval) {
        super(maxCapatity, evictExpiredInterval);
    }

    @Override
    protected void addToCache(Entry<K, V> entry) {
        writeLock.lock();
        index.get(entry.getExpireTime()).add(entry.getKey());
        writeLock.unlock();
    }

    @Override
    protected void removeFromCache(Entry<K, V> entry) {
        writeLock.lock();
        index.get(entry.getExpireTime()).remove(entry.getKey());
        writeLock.unlock();
    }

    @Override
    protected void beforeRecomputeExpireTimeOnRead(Entry<K, V> entry) {
        removeFromCache(entry);
    }

    @Override
    protected void afterRecomputeExpireTimeOnRead(Entry<K, V> entry) {
        addToCache(entry);
    }

    @Override
    protected void clearExpired() {
        long now = System.currentTimeMillis();
        List<Long> expireTimes = new ArrayList<Long>();
        Set<Long> set = null;
        readLock.lock();
        set = index.keySet();
        readLock.unlock();
        expireTimes.addAll(set);
        for (Long expireTime : expireTimes) {
            if (expireTime > now) {
                break;
            }
            readLock.lock();
            List<K> keys = new ArrayList<K>(index.get(expireTime));
            readLock.unlock();
            Collects.forEach(keys, new Consumer2<Integer, K>() {
                @Override
                public void accept(Integer expireTime, K key) {
                    remove(key, RemoveCause.COLLECTED);
                }
            });
        }
    }

}
