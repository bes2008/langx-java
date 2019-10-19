package com.jn.langx.cache;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.struct.Holder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractCache<K, V> implements Cache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCache.class);
    private ConcurrentHashMap<K, Entry<K, V>> map;
    private Loader<K, V> globalLoader;
    // unit: seconds
    private long expireAfterWrite = Long.MAX_VALUE;
    // unit: seconds
    private long expireAfterRead = Long.MAX_VALUE;
    // unit: seconds
    private long refreshAfterAccess = Long.MAX_VALUE;
    // unit: mills
    private long evictExpiredInterval;
    // unit: mills
    private long nextEvictExpiredTime;

    private RemoveListener<K, V> removeListener;
    private int maxCapacity;
    private float capacityHeightWater = 0.95f;

    /**
     * Key: expire time
     * Value: entry.key
     */
    private Map<Long, List<K>> expireTimeIndex = WrappedNonAbsentMap.wrap(new TreeMap<Long, List<K>>(new ComparableComparator<Long>()), new Supplier<Long, List<K>>() {
        @Override
        public List<K> get(Long expireTime) {
            return Collects.emptyLinkedList();
        }
    });

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    protected AbstractCache(int maxCapacity, long evictExpiredInterval) {
        this.evictExpiredInterval = evictExpiredInterval;
        Preconditions.checkTrue(evictExpiredInterval >= 0);
        this.maxCapacity = maxCapacity;
        computeNextEvictExpiredTime();
    }

    void computeNextEvictExpiredTime() {
        long now = System.currentTimeMillis();
        if (evictExpiredInterval < 0) {
            nextEvictExpiredTime = Long.MAX_VALUE;
        }
        nextEvictExpiredTime = Dates.nextTime(evictExpiredInterval);
    }

    @Override
    public void set(@NonNull K key, @Nullable V value) {
        set(key, value, expireAfterWrite, TimeUnit.SECONDS);
    }

    @Override
    public void set(@NonNull K key, @Nullable V value, long duration, TimeUnit timeUnit) {
        Preconditions.checkTrue(duration >= 0);
        duration = timeUnit.toMillis(duration);
        set(key, value, Dates.nextTime(duration));
    }

    @Override
    public void set(@NonNull K key, @Nullable V value, long expire) {
        Preconditions.checkNotNull(key);
        Preconditions.checkTrue(expire > 0);
        evictExpired();
        long now = System.currentTimeMillis();
        if (value == null) {
            remove(key, RemoveCause.EXPLICIT);
        } else if (expire < now) {
            remove(key, RemoveCause.EXPRIED);
        } else {
            writeLock.lock();
            try {
                remove(key, RemoveCause.REPLACED);
                Entry<K, V> entry = new Entry<K, V>(key, value, expire);
                map.put(key, entry);
                expireTimeIndex.get(entry.getExpireTime()).add(entry.getKey());
                addToCache(entry);
            } finally {
                writeLock.unlock();
            }
        }
    }

    protected abstract void addToCache(Entry<K, V> entry);

    @Override
    public V get(@NonNull K key) {
        return get(key, null);
    }

    @Override
    public V getIfPresent(@NonNull K key) {
        return get(key, null, false);
    }

    @Override
    public V get(@NonNull K key, @Nullable Supplier<K, V> loader) {
        return get(key, loader, true);
    }

    private V get(@NonNull K key, @Nullable Supplier<K, V> loader, boolean loadIfAbsent) {
        evictExpired();
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            if (!entry.isExpired()) {
                long nextRefreshTime = Dates.nextTime(entry.getLastUsedTime(), TimeUnit.SECONDS.toMillis(refreshAfterAccess));
                if (System.currentTimeMillis() > nextRefreshTime) {
                    return refresh(key, true);
                }

                entry.incrementUseCount();
                if (expireAfterRead > 0) {
                    writeLock.lock();
                    try {
                        expireTimeIndex.get(entry.getExpireTime()).remove(entry.getKey());
                        beforeRecomputeExpireTimeOnRead(entry);
                        entry.setExpireTime(Dates.nextTime(expireAfterRead));
                        expireTimeIndex.get(entry.getExpireTime()).add(entry.getKey());
                        afterRecomputeExpireTimeOnRead(entry);
                    } finally {
                        writeLock.unlock();
                    }
                }

                return entry.getValue();
            } else {
                remove(key, RemoveCause.EXPRIED);
            }
        }
        V value = null;
        if (loadIfAbsent) {
            if (loader != null) {
                try {
                    value = loader.get(key);
                } catch (Throwable ex) {
                    logger.warn("Error occur when load resource for key: {}, error message: {}, stack:", key, ex.getMessage(), ex);
                }
            } else {
                Holder<Throwable> exceptionHolder = new Holder<Throwable>();
                value = loadByGlobalLoader(key, exceptionHolder);
                if (value == null) {
                    if (exceptionHolder.get() != null) {
                        logger.warn("Error occur when load resource for key: {}, error message: {}, stack:", key, exceptionHolder.get().getMessage(), exceptionHolder.get());
                    } else {
                        remove(key, RemoveCause.REPLACED);
                    }
                }
            }

            if (value != null) {
                set(key, value);
            }
        }
        entry = map.get(key);
        if (entry != null) {
            entry.incrementUseCount();
            try {
                writeLock.lock();
                afterRecomputeExpireTimeOnRead(entry);
            } finally {
                writeLock.unlock();
            }
        }
        return value;
    }

    protected abstract void beforeRecomputeExpireTimeOnRead(Entry<K, V> entry);

    protected abstract void afterRecomputeExpireTimeOnRead(Entry<K, V> entry);

    @Override
    public void refresh(@NonNull K key) {
        refresh(key, false);
    }

    private V refresh(@NonNull K key, boolean internalInvoke) {
        Preconditions.checkNotNull(key);
        evictExpired();
        Holder<Throwable> exceptionHolder = new Holder<Throwable>();
        V value = loadByGlobalLoader(key, exceptionHolder);
        if (value == null) {
            if (exceptionHolder.get() != null) {
                logger.warn("Error occur when load resource for key: {}, error message: {}, stack:", key, exceptionHolder.get().getMessage(), exceptionHolder.get());
            } else {
                remove(key, internalInvoke ? RemoveCause.REPLACED : RemoveCause.EXPLICIT);
            }
        } else {
            set(key, value);
        }
        return get(key);
    }

    private V loadByGlobalLoader(@NonNull K key, @NonNull Holder<Throwable> error) {
        V value = null;
        if (globalLoader != null) {
            try {
                value = globalLoader.load(key);
            } catch (Throwable ex) {
                error.set(ex);
            }
        }
        return value;
    }

    protected abstract void removeFromCache(Entry<K, V> entry, RemoveCause removeCause);

    protected final V remove(@NonNull K key, @NonNull RemoveCause cause) {
        V ret = null;
        writeLock.lock();
        try {
            Entry<K, V> entry = map.remove(key);
            ret = entry == null ? null : entry.getValue();
            if (ret != null) {
                expireTimeIndex.get(entry.getExpireTime()).remove(entry.getKey());
                removeFromCache(entry, cause);
            }
        } finally {
            writeLock.unlock();
        }

        if (ret != null && removeListener != null) {
            removeListener.onRemove(key, ret, cause);
        }
        return ret;
    }

    @Override
    public V remove(@NonNull K key) {
        evictExpired();
        return remove(key, RemoveCause.EXPLICIT);
    }


    private void evictExpired() {
        if ((evictExpiredInterval >= 0 && System.currentTimeMillis() >= nextEvictExpiredTime) || (map.size() > maxCapacity * capacityHeightWater)) {
            clearExpired();
            int forceEvictCount = map.size() - new Float(maxCapacity * capacityHeightWater).intValue();
            if (forceEvictCount > 0) {
                writeLock.lock();
                try {
                    List<Entry<K, V>> cleared = forceEvict(forceEvictCount);
                    Collects.forEach(cleared, new Consumer<Entry<K, V>>() {
                        @Override
                        public void accept(Entry<K, V> entry) {
                            expireTimeIndex.get(entry.getExpireTime()).remove(entry.getKey());
                        }
                    });
                } finally {
                    writeLock.unlock();
                }

            }
            Collects.forEach(map, new Consumer2<K, Entry<K, V>>() {
                @Override
                public void accept(K key, Entry<K, V> entry) {
                    entry.incrementAge();
                }
            });
        }
    }

    protected abstract List<Entry<K, V>> forceEvict(int count);

    private void clearExpired() {
        long now = System.currentTimeMillis();
        List<Long> expireTimes = new ArrayList<Long>();
        Set<Long> set = null;
        readLock.lock();
        set = expireTimeIndex.keySet();
        readLock.unlock();
        expireTimes.addAll(set);
        for (Long expireTime : expireTimes) {
            if (expireTime > now) {
                break;
            }
            readLock.lock();
            List<K> keys = new ArrayList<K>(expireTimeIndex.get(expireTime));
            readLock.unlock();
            Collects.forEach(keys, new Consumer2<Integer, K>() {
                @Override
                public void accept(Integer expireTime, K key) {
                    remove(key, RemoveCause.COLLECTED);
                }
            });
        }
    }

    @Override
    public void clean() {
        map.clear();
    }

    @Override
    public int size() {
        evictExpired();
        return map.size();
    }

    @Override
    public Map<K, V> toMap() {
        final Map<K, V> map = new HashMap<K, V>();
        Collects.forEach(this.map, new Consumer2<K, Entry<K, V>>() {
            @Override
            public void accept(K key, Entry<K, V> entry) {
                map.put(key, entry.getValue());
            }
        });
        return map;
    }

    void setMap(ConcurrentHashMap<K, Entry<K, V>> map) {
        this.map = map;
    }

    void setGlobalLoader(Loader<K, V> globalLoader) {
        this.globalLoader = globalLoader;
    }

    void setExpireAfterWrite(long expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    void setExpireAfterRead(long expireAfterRead) {
        this.expireAfterRead = expireAfterRead;
    }

    void setEvictExpiredInterval(long evictExpiredInterval) {
        this.evictExpiredInterval = evictExpiredInterval;
    }

    void setRemoveListener(RemoveListener<K, V> removeListener) {
        this.removeListener = removeListener;
    }

    void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    void setCapacityHeightWater(float capacityHeightWater) {
        this.capacityHeightWater = capacityHeightWater;
    }

    void setRefreshAfterAccess(long refreshAfterAccess) {
        this.refreshAfterAccess = refreshAfterAccess;
    }
}
