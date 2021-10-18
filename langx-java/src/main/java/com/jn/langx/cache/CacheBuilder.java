package com.jn.langx.cache;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.reference.ReferenceType;
import com.jn.langx.util.timing.timer.Timer;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.TimeUnit;

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
    private long refreshAllInterval = TimeUnit.HOURS.toMillis(1);

    // unit: mills
    private long evictExpiredInterval = 5 * 60 * 1000;
    private RemoveListener<K, V> removeListener;
    private int maxCapacity = Integer.MAX_VALUE;

    private Timer timer;

    private float capacityHeightWater = 0.95f;

    private ReferenceType keyReferenceType = ReferenceType.STRONG;
    private ReferenceType valueReferenceType = ReferenceType.STRONG;

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
        this.capacityHeightWater = capatityHeightWater;
        return this;
    }

    public CacheBuilder<K, V> refreshAfterAccess(long refreshAfterAccess) {
        this.refreshAfterAccess = refreshAfterAccess;
        return this;
    }

    public CacheBuilder<K, V> weakValue(boolean weakValue) {
        if (weakValue) {
            this.valueReferenceType = ReferenceType.WEAK;
        }
        return this;
    }

    public CacheBuilder<K, V> softValue(boolean softValue) {
        if (softValue) {
            this.valueReferenceType = ReferenceType.SOFT;
        }
        return this;
    }

    public CacheBuilder<K, V> weakKey(boolean weakKey) {
        if (weakKey) {
            this.keyReferenceType = ReferenceType.WEAK;
        }
        return this;
    }

    public CacheBuilder<K, V> softKey(boolean softKey) {
        if (softKey) {
            this.keyReferenceType = ReferenceType.SOFT;
        }
        return this;
    }

    public Cache<K, V> build() {
        Preconditions.checkNotNull(cacheClass, "Please specify your cache class");
        Preconditions.checkTrue(Reflects.isSubClass(AbstractCache.class, cacheClass), StringTemplates.formatWithPlaceholder("Your cache calss {} is not a subclass of {}", Reflects.getFQNClassName(cacheClass), Reflects.getFQNClassName(AbstractCache.class)));
        AbstractCache<K, V> cache = Reflects.<AbstractCache<K, V>>newInstance(cacheClass);
        Preconditions.checkNotNull(cache);
        cache.setExpireAfterRead(expireAfterRead);
        cache.setExpireAfterWrite(expireAfterWrite);
        cache.setRefreshAfterAccess(refreshAfterAccess < 0 ? Long.MAX_VALUE : refreshAfterAccess);
        cache.setGlobalLoader(loader);
        cache.setMaxCapacity(maxCapacity < 0 ? Integer.MAX_VALUE : maxCapacity);
        cache.setEvictExpiredInterval(evictExpiredInterval < 0 ? Long.MAX_VALUE : evictExpiredInterval);
        cache.setRefreshAllInterval(refreshAllInterval);
        cache.setCapacityHeightWater(capacityHeightWater <= 0 ? 0.95f : capacityHeightWater);
        // value is ReferenceEntry, so here is STRONG
        ConcurrentReferenceHashMap<K, Entry<K, V>> map = new ConcurrentReferenceHashMap<K, Entry<K, V>>(initialCapacity, 16, concurrencyLevel, keyReferenceType, ReferenceType.STRONG);
        cache.setKeyReferenceType(keyReferenceType);
        cache.setValueReferenceType(valueReferenceType);
        if (keyReferenceType != ReferenceType.STRONG || valueReferenceType != ReferenceType.STRONG) {
            cache.setReferenceQueue(new ReferenceQueue());
        }
        cache.setMap(map);
        cache.setRemoveListener(removeListener);
        if (evictExpiredInterval > 0 && timer != null) {
            cache.setTimer(timer);
        }
        cache.startup();
        return cache;
    }
}
