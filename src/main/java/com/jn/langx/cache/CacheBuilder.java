package com.jn.langx.cache;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Reflects;

import java.util.concurrent.ConcurrentHashMap;

public class CacheBuilder<K, V> {
    private Class cacheClass;
    private int concurrencyLevel = Runtime.getRuntime().availableProcessors();
    private int initialCapacity;

    private Loader<K, V> loader;
    // unit: seconds
    private long expireAfterWrite = -1;
    // unit: seconds
    private long expireAfterRead = -1;
    // unit: mills
    private long evictExpiredInterval = Long.MAX_VALUE;
    private RemoveListener<K, V> removeListener;
    private int maxCapacity = Integer.MAX_VALUE;

    private float capatityHeightWater = 0.95f;

    private CacheBuilder() {

    }

    public static <K, V> CacheBuilder<K, V> newBuilder() {
        return new CacheBuilder();
    }

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

    public CacheBuilder<K, V> maxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

    public CacheBuilder<K, V> capatityHeightWater(float capatityHeightWater) {
        this.capatityHeightWater = capatityHeightWater;
        return this;
    }

    public Cache<K, V> build() {
        Preconditions.checkNotNull(cacheClass, "Please specify your cache class");
        Preconditions.checkTrue(Reflects.isSubClassOrEquals(AbstractCache.class, cacheClass), StringTemplates.formatWithPlaceholder("Your cache calss {} is not a subclass of {}", Reflects.getFQNClassName(cacheClass), Reflects.getFQNClassName(AbstractCache.class)));
        AbstractCache<K, V> cache = Reflects.<AbstractCache<K, V>>newInstance(cacheClass);
        Preconditions.checkNotNull(cache);
        cache.setExpireAfterRead(expireAfterRead < 0 ? 60 : evictExpiredInterval);
        cache.setExpireAfterWrite(expireAfterWrite < 0 ? 60 : evictExpiredInterval);
        cache.setGlobalLoader(loader);
        cache.setMaxCapacity(maxCapacity);
        cache.setEvictExpiredInterval(evictExpiredInterval < 0 ? Long.MAX_VALUE : evictExpiredInterval);
        cache.setCapatityHeightWater(capatityHeightWater);
        ConcurrentHashMap<K, Entry<K, V>> map = new ConcurrentHashMap<K, Entry<K, V>>(initialCapacity, 16, concurrencyLevel);
        cache.setMap(map);
        cache.setRemoveListener(removeListener);
        cache.computeNextEvictExpiredTime();
        return cache;
    }
}
