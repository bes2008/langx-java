package com.jn.langx.cache;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.timing.timer.Timer;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"unused"})
public class CacheBuilder<K, V> {
    private Class cacheClass = LRUCache.class;
    private int concurrencyLevel = Runtime.getRuntime().availableProcessors();
    private int initialCapacity;

    private Loader<K, V> loader;
    // unit: seconds
    private long expireAfterWrite = -1;
    // unit: seconds
    private long expireAfterRead = -1;
    // unit: seconds
    private long refreshAfterAccess = Long.MAX_VALUE;

    // unit: mills
    private long evictExpiredInterval = Long.MAX_VALUE;
    private RemoveListener<K, V> removeListener;
    private int maxCapacity = Integer.MAX_VALUE;

    private Timer timer;

    private float capatityHeightWater = 0.95f;

    private CacheBuilder() {

    }

    public static <K, V> CacheBuilder<K, V> newBuilder() {
        return new CacheBuilder<K, V>();
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

    public CacheBuilder<K, V> expireAfterWrite(long expireAfterWriteInSeconds) {
        this.expireAfterWrite = expireAfterWriteInSeconds;
        return this;
    }

    public CacheBuilder<K, V> expireAfterRead(long expireAfterReadInSeconds) {
        this.expireAfterRead = expireAfterReadInSeconds;
        return this;
    }

    /**
     * Set the period of global evict, default : Long.MAX_VALUE
     * unit: mills
     *
     * @param evictExpiredIntervalInMills the evict period
     * @return the cache builder
     */
    public CacheBuilder<K, V> evictExpiredInterval(long evictExpiredIntervalInMills, Timer timer) {
        return evictExpiredInterval(evictExpiredIntervalInMills).timer(timer);
    }

    public CacheBuilder<K, V> evictExpiredInterval(long evictExpiredIntervalInMills) {
        this.evictExpiredInterval = evictExpiredIntervalInMills;
        return this;
    }

    public CacheBuilder<K, V> timer(Timer timer) {
        this.timer = timer;
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

    public CacheBuilder<K, V> capacityHeightWater(float capatityHeightWater) {
        this.capatityHeightWater = capatityHeightWater;
        return this;
    }

    public CacheBuilder<K, V> refreshAfterAccess(long refreshAfterAccess) {
        this.refreshAfterAccess = refreshAfterAccess;
        return this;
    }

    public Cache<K, V> build() {
        Preconditions.checkNotNull(cacheClass, "Please specify your cache class");
        Preconditions.checkTrue(Reflects.isSubClass(AbstractCache.class, cacheClass), StringTemplates.formatWithPlaceholder("Your cache calss {} is not a subclass of {}", Reflects.getFQNClassName(cacheClass), Reflects.getFQNClassName(AbstractCache.class)));
        AbstractCache<K, V> cache = Reflects.<AbstractCache<K, V>>newInstance(cacheClass);
        Preconditions.checkNotNull(cache);
        cache.setExpireAfterRead(expireAfterRead < 0 ? Long.MAX_VALUE : expireAfterRead);
        cache.setExpireAfterWrite(expireAfterWrite < 0 ? Long.MAX_VALUE : expireAfterWrite);
        cache.setRefreshAfterAccess(refreshAfterAccess < 0 ? Long.MAX_VALUE : refreshAfterAccess);
        cache.setGlobalLoader(loader);
        cache.setMaxCapacity(maxCapacity < 0 ? Integer.MAX_VALUE : maxCapacity);
        cache.setEvictExpiredInterval(evictExpiredInterval < 0 ? Long.MAX_VALUE : evictExpiredInterval);
        cache.setCapacityHeightWater(capatityHeightWater <= 0 ? 0.95f : capatityHeightWater);
        ConcurrentHashMap<K, Entry<K, V>> map = new ConcurrentHashMap<K, Entry<K, V>>(initialCapacity, 16, concurrencyLevel);
        cache.setMap(map);
        cache.setRemoveListener(removeListener);
        if (evictExpiredInterval > 0 && timer != null) {
            cache.setTimer(timer);
        }
        cache.startup();
        return cache;
    }
}
