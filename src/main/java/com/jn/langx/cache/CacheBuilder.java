package com.jn.langx.cache;

import com.jn.langx.util.reflect.Reflects;

import java.util.concurrent.ConcurrentHashMap;

public class CacheBuilder<K, V> {
    private Class cacheClass;
    private int concurrencyLevel = Runtime.getRuntime().availableProcessors();
    private int initialCapacity;
    private int maxCapacity = Integer.MAX_VALUE;

    private Loader<K, V> loader;
    // unit: seconds
    private long expireAfterWrite = Long.MAX_VALUE;
    // unit: seconds
    private long expireAfterRead = Long.MAX_VALUE;
    // unit: mills
    private long evictExpiredInterval = Long.MAX_VALUE;
    private RemoveListener<K, V> removeListener;
    private int maxCapatity;
    private float capatityHeightWater = 0.95f;

    public CacheBuilder<K, V> cacheClass(Class cacheClass) {
        this.cacheClass = cacheClass;
        return this;
    }

    public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }

    public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
        return this;
    }

    public CacheBuilder<K, V> maxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

    public CacheBuilder<K, V> loader(Loader<K, V> loader) {
        this.loader = loader;
        return this;
    }

    public CacheBuilder<K, V> expireAfterWrite(long expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
        return this;
    }

    public CacheBuilder<K, V> expireAfterRead(long expireAfterRead) {
        this.expireAfterRead = expireAfterRead;
        return this;
    }

    public CacheBuilder<K, V> evictExpiredInterval(long evictExpiredInterval) {
        this.evictExpiredInterval = evictExpiredInterval;
        return this;
    }

    public CacheBuilder<K, V> removeListener(RemoveListener<K, V> removeListener) {
        this.removeListener = removeListener;
        return this;
    }

    public CacheBuilder<K, V> maxCapatity(int maxCapatity) {
        this.maxCapatity = maxCapatity;
        return this;
    }

    public CacheBuilder<K, V> capatityHeightWater(float capatityHeightWater) {
        this.capatityHeightWater = capatityHeightWater;
        return this;
    }

    public Cache<K, V> build() {
        AbstractCache<K, V> cache = Reflects.<AbstractCache<K, V>>newInstance(cacheClass);
        cache.setExpireAfterRead(expireAfterRead);
        cache.setExpireAfterWrite(expireAfterWrite);
        cache.setGlobalLoader(loader);
        cache.setMaxCapatity(maxCapatity);
        cache.setEvictExpiredInterval(evictExpiredInterval < 0 ? Long.MAX_VALUE : evictExpiredInterval);
        cache.setCapatityHeightWater(capatityHeightWater);
        ConcurrentHashMap<K, Entry<K, V>> map = new ConcurrentHashMap<K, Entry<K, V>>(initialCapacity, 16, concurrencyLevel);
        cache.setMap(map);
        cache.setRemoveListener(removeListener);
        return cache;
    }
}
