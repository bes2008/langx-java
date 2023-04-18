package com.jn.langx.cache;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.collection.iter.EnumerationIterable;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.reference.ReferenceType;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.Timer;
import org.slf4j.Logger;

import java.lang.ref.ReferenceQueue;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractCache<K, V> extends BaseCache<K, V> {
    private ConcurrentReferenceHashMap<K, Entry<K, V>> map;
    private Loader<K, V> globalLoader;
    // unit: seconds, 大于 0 时有效，用于在发生了写操作时，更新过期时间。 < 0 时代表无过期时间，即永不过期
    private long expireAfterWrite = -1;
    // unit: seconds, 大于 0 时有效，用于在发生了写操作时，更新过期时间。<=0 时，代表不启用该特性
    private long expireAfterRead = -1;
    // unit: seconds, 大于 0 时有效。 用于在一个key没有过期时，对它进行 reload 操作
    private long refreshAfterAccess = -1;
    private RemoveListener<K, V> removeListener;
    private int maxCapacity;
    private float capacityHeightWater = 0.95f;


    private ReferenceType keyReferenceType;
    private ReferenceType valueReferenceType;
    private ReferenceQueue referenceQueue;

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
    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    protected AbstractCache(int maxCapacity, long evictExpiredInterval) {
        this(maxCapacity, evictExpiredInterval, null);
    }

    protected AbstractCache(int maxCapacity, long evictExpiredInterval, Timer timer) {
        this(maxCapacity, evictExpiredInterval, 0, timer);
    }

    protected AbstractCache(int maxCapacity, long evictExpiredInterval, long refreshAllInterval, Timer timer) {
        this.evictExpiredInterval = evictExpiredInterval;
        this.refreshAllInterval = refreshAllInterval;
        Preconditions.checkTrue(evictExpiredInterval >= 0);
        this.maxCapacity = maxCapacity;
        computeNextEvictExpiredTime();
        computeNextRefreshAllTime();
        this.timer = timer;
    }

    public void setKeyReferenceType(ReferenceType keyReferenceType) {
        this.keyReferenceType = keyReferenceType;
    }

    public void setValueReferenceType(ReferenceType valueReferenceType) {
        this.valueReferenceType = valueReferenceType;
    }

    public void setReferenceQueue(ReferenceQueue referenceQueue) {
        this.referenceQueue = referenceQueue;
    }

    @Override
    public void set(@NonNull K key, @Nullable V value) {
        set(key, value, expireAfterWrite, TimeUnit.SECONDS);
    }

    @Override
    public void set(@NonNull K key, @Nullable V value, long duration, TimeUnit timeUnit) {
        if (duration >= 0) {
            duration = timeUnit.toMillis(duration);
        }
        set(key, value, Dates.nextTime(duration));
    }

    @Override
    public void set(@NonNull K key, @Nullable V value, long expire) {
        Preconditions.checkNotNull(key);
        Preconditions.checkTrue(expire > 0);
        if (!running) {
            return;
        }
        evictExpired();
        long now = System.currentTimeMillis();
        if (value == null) {
            remove(key, RemoveCause.EXPLICIT);
        } else if (expire < now) {
            remove(key, RemoveCause.EXPIRED);
        } else {
            writeLock.lock();
            try {
                remove(key, RemoveCause.REPLACED);
                Entry<K, V> entry = new Entry<K, V>(key, keyReferenceType, value, valueReferenceType, referenceQueue, false, expire);
                map.put(key, entry);
                expireTimeIndex.get(entry.getExpireTime()).add(entry.getKey());
                addToCache(entry);
            } finally {
                writeLock.unlock();
            }
        }
    }

    protected abstract void addToCache(Entry<K, V> entry);

    protected void incrementUsedCount(Entry<K, V> entry) {
        entry.incrementUseCount();
        entry.incrementAge();
    }

    @Override
    public V get(@NonNull K key) {
        return get(key, null);
    }

    @Override
    public Map<K, V> getAll(Iterable<K> keys) {
        final Map<K, V> m = new HashMap<K, V>();
        Collects.forEach(keys, new Consumer<K>() {
            @Override
            public void accept(K key) {
                V v = get(key);
                m.put(key, v);
            }
        });
        return m;
    }

    @Override
    public Map<K, V> getAllIfPresent(Iterable<K> keys) {
        final Map<K, V> amap = new HashMap<K, V>();
        Collects.forEach(keys, new Consumer<K>() {
            @Override
            public void accept(K key) {
                V v = getIfPresent(key);
                amap.put(key, v);
            }
        });
        return amap;
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
        if (!running) {
            return null;
        }
        evictExpired();
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            if (!entry.isExpired()) {
                if (refreshAfterAccess > 0) {
                    long nextRefreshTime = Dates.nextTime(entry.getLastUsedTime(), TimeUnit.SECONDS.toMillis(refreshAfterAccess));
                    if (System.currentTimeMillis() > nextRefreshTime) {
                        return refresh(key, true);
                    }
                }
                incrementUsedCount(entry);
                if (expireAfterRead > 0) {
                    writeLock.lock();
                    try {
                        K key0 = entry.getKey();
                        if (key0 != null) {
                            expireTimeIndex.get(entry.getExpireTime()).remove(key0);
                            beforeRecomputeExpireTimeOnRead(entry);
                            entry.setExpireTime(Dates.nextTime(TimeUnit.SECONDS.toMillis(expireAfterRead)));
                            expireTimeIndex.get(entry.getExpireTime()).add(key0);
                            afterRecomputeExpireTimeOnRead(entry);
                        }
                    } finally {
                        writeLock.unlock();
                    }
                }
                V v = null;
                beforeRead(entry);
                v = entry.getValue();
                afterRead(entry);
                return v;
            } else {
                remove(key, RemoveCause.EXPIRED);
            }
        }
        V value = null;
        if (loadIfAbsent) {
            Logger logger = Loggers.getLogger(getClass());
            String errorMessage = "Error occur when load resource for key: {}, error message: {}, stack:";
            if (loader != null) {
                try {
                    value = loader.get(key);
                } catch (Throwable ex) {
                    logger.warn(errorMessage, key, ex.getMessage(), ex);
                }
            } else {
                Holder<Throwable> exceptionHolder = new Holder<Throwable>();
                value = loadByGlobalLoader(key, exceptionHolder);
                if (value == null) {
                    if (exceptionHolder.get() != null) {
                        logger.warn(errorMessage, key, exceptionHolder.get().getMessage(), exceptionHolder.get());
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
        if (!running) {
            return null;
        }
        Preconditions.checkNotNull(key);
        Holder<Throwable> exceptionHolder = new Holder<Throwable>();
        V value = loadByGlobalLoader(key, exceptionHolder);
        if (value == null) {
            if (exceptionHolder.get() != null) {
                Logger logger = Loggers.getLogger(getClass());
                logger.warn("Error occur when load resource for key: {}, error message: {}, stack:", key, exceptionHolder.get().getMessage(), exceptionHolder.get());
            } else {
                remove(key, internalInvoke ? RemoveCause.REPLACED : RemoveCause.EXPLICIT);
            }
        } else {
            set(key, value);
        }
        return get(key);
    }


    /**
     * @param timeout
     * @since 4.0.4
     */
    @Override
    protected void refreshAllAsync(@Nullable final Timeout timeout) {
        Set<K> keys = keys();
        Collects.forEach(keys, new Consumer<K>() {
            @Override
            public void accept(K key) {
                /**
                 * @since 4.0.5
                 * 利用 timer 可以再次 异步执行
                 */
                if (timer != null) {
                    timer.newTimeout(new RefreshKeyTask(key), 1, TimeUnit.MILLISECONDS);
                } else {
                    refresh(key);
                }
            }
        }, new Predicate<K>() {
            @Override
            public boolean test(K key) {
                if (timeout != null && timeout.isCancelled()) {
                    return true;
                }
                return false;
            }
        });
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

    protected abstract void beforeRead(Entry<K, V> entry);

    protected abstract void afterRead(Entry<K, V> entry);

    protected final V remove(@NonNull K key, @NonNull RemoveCause cause) {
        V ret = null;
        writeLock.lock();
        try {
            Entry<K, V> entry = map.remove(key);
            if (entry != null) {
                expireTimeIndex.get(entry.getExpireTime()).remove(entry.getKey());
                ret = entry.getValue(false);
            }
            if (ret != null) {
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

    @Override
    public List<V> remove(Collection<K> keys) {
        final List<V> list = Collects.emptyArrayList();
        Collects.forEach(keys, new Consumer<K>() {
            @Override
            public void accept(K key) {
                V v = remove(key);
                list.add(v);
            }
        });
        return list;
    }

    public void evictExpired() {
        if ((evictExpiredInterval >= 0 && System.currentTimeMillis() >= nextEvictExpiredTime) || (map.size() > maxCapacity * capacityHeightWater)) {
            clearExpired();
            int forceEvictCount = map.size() - Numbers.toInt(maxCapacity * capacityHeightWater);
            if (forceEvictCount > 0) {
                writeLock.lock();
                try {
                    List<K> cleared = forceEvict(forceEvictCount);
                    Collects.forEach(cleared, new Consumer<K>() {
                        @Override
                        public void accept(K key) {
                            remove(key, RemoveCause.REPLACED);
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

    /**
     * 用于找到将被强制清除的
     *
     * @param count the count will be evicted
     */
    protected abstract List<K> forceEvict(int count);

    private void clearExpired() {
        long now = System.currentTimeMillis();
        writeLock.lock();
        try {
            List<Long> expireTimes = new ArrayList<Long>(expireTimeIndex.keySet());
            for (Long expireTime : expireTimes) {
                if (expireTime > now) {
                    break;
                }
                List<K> keys = new ArrayList<K>(expireTimeIndex.get(expireTime));
                expireTimeIndex.remove(expireTime);
                Collects.forEach(keys, new Consumer2<Integer, K>() {
                    @Override
                    public void accept(Integer expireTime, K key) {
                        remove(key, RemoveCause.EXPIRED);
                    }
                });
            }
        } finally {
            computeNextEvictExpiredTime();
            writeLock.unlock();
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

    /**
     * @since 4.0.4
     */
    @Override
    public Set<K> keys() {
        return Collects.asSet(new EnumerationIterable<K>(this.map.keys()));
    }

    @Override
    public Map<K, V> toMap() {
        final Map<K, V> m = new HashMap<K, V>();
        Collects.forEach(this.map, new Consumer2<K, Entry<K, V>>() {
            @Override
            public void accept(K key, Entry<K, V> entry) {
                m.put(key, entry.getValue(false));
            }
        });
        return m;
    }

    protected void setMap(ConcurrentReferenceHashMap<K, Entry<K, V>> map) {
        this.map = map;
    }

    protected void setGlobalLoader(Loader<K, V> globalLoader) {
        this.globalLoader = globalLoader;
    }

    protected void setExpireAfterWrite(long expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    protected void setExpireAfterRead(long expireAfterRead) {
        this.expireAfterRead = expireAfterRead;
    }

    protected void setEvictExpiredInterval(long evictExpiredInterval) {
        this.evictExpiredInterval = evictExpiredInterval;
    }

    protected void setRemoveListener(RemoveListener<K, V> removeListener) {
        this.removeListener = removeListener;
    }

    protected void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    protected void setCapacityHeightWater(float capacityHeightWater) {
        this.capacityHeightWater = capacityHeightWater;
    }

    protected void setRefreshAfterAccess(long refreshAfterAccess) {
        this.refreshAfterAccess = refreshAfterAccess;
    }

    protected void setRefreshAllInterval(long refreshAllInterval) {
        this.refreshAllInterval = refreshAllInterval;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
