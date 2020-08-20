package com.jn.langx.cache;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.lifecycle.Lifecycle;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.reference.ReferenceType;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.ReferenceQueue;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractCache<K, V> implements Cache<K, V>, Lifecycle {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCache.class);
    private ConcurrentReferenceHashMap<K, Entry<K, V>> map;
    private Loader<K, V> globalLoader;
    // unit: seconds
    private long expireAfterWrite = Long.MAX_VALUE;
    // unit: seconds
    private long expireAfterRead = Long.MAX_VALUE;
    // unit: seconds
    private long refreshAfterAccess = Long.MAX_VALUE;
    // unit: mills
    private volatile long evictExpiredInterval;
    // unit: mills
    private volatile long nextEvictExpiredTime;

    private RemoveListener<K, V> removeListener;
    private int maxCapacity;
    private float capacityHeightWater = 0.95f;

    private Timer timer;
    private boolean shutdownTimerSelf = false;

    private volatile boolean running = false;

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
    private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    protected AbstractCache(int maxCapacity, long evictExpiredInterval) {
        this(maxCapacity, evictExpiredInterval, null);
    }

    protected AbstractCache(int maxCapacity, long evictExpiredInterval, Timer timer) {
        this.evictExpiredInterval = evictExpiredInterval;
        Preconditions.checkTrue(evictExpiredInterval >= 0);
        this.maxCapacity = maxCapacity;
        computeNextEvictExpiredTime();
        this.timer = timer;
    }

    class EvictExpiredTask implements TimerTask {
        @Override
        public void run(Timeout timeout) throws Exception {
            if (timeout.isCancelled()) {
                // NOOP
            } else {
                evictExpired();
                timer.newTimeout(this, nextEvictExpiredTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            }
        }
    }

    void computeNextEvictExpiredTime() {
        if (evictExpiredInterval < 0) {
            nextEvictExpiredTime = Long.MAX_VALUE;
        }
        nextEvictExpiredTime = Dates.nextTime(evictExpiredInterval);
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
        Preconditions.checkTrue(duration >= 0);
        duration = timeUnit.toMillis(duration);
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
        final Map<K, V> map = new HashMap<K, V>();
        Collects.forEach(keys, new Consumer<K>() {
            @Override
            public void accept(K key) {
                V v = get(key);
                map.put(key, v);
            }
        });
        return map;
    }

    @Override
    public Map<K, V> getAllIfPresent(Iterable<K> keys) {
        final Map<K, V> map = new HashMap<K, V>();
        Collects.forEach(keys, new Consumer<K>() {
            @Override
            public void accept(K key) {
                V v = getIfPresent(key);
                map.put(key, v);
            }
        });
        return map;
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
                long nextRefreshTime = Dates.nextTime(entry.getLastUsedTime(), TimeUnit.SECONDS.toMillis(refreshAfterAccess));
                if (System.currentTimeMillis() > nextRefreshTime) {
                    return refresh(key, true);
                }

                incrementUsedCount(entry);
                if (expireAfterRead > 0) {
                    writeLock.lock();
                    try {
                        K key0 = entry.getKey();
                        if (key0 != null) {
                            expireTimeIndex.get(entry.getExpireTime()).remove(key0);
                            beforeRecomputeExpireTimeOnRead(entry);
                            entry.setExpireTime(Dates.nextTime(expireAfterRead));
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
        if (!running) {
            return null;
        }
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

    protected abstract void beforeRead(Entry<K, V> entry);

    protected abstract void afterRead(Entry<K, V> entry);

    protected final V remove(@NonNull K key, @NonNull RemoveCause cause) {
        V ret = null;
        writeLock.lock();
        try {
            Entry<K, V> entry = map.remove(key);
            if (entry != null) {
                ret = entry.getValue(false);
            }
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
                            K key = entry.getKey();
                            if (key != null) {
                                remove(key, RemoveCause.REPLACED);
                            }
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
        writeLock.lock();
        try {
            List<Long> expireTimes = new ArrayList<Long>(expireTimeIndex.keySet());
            for (Long expireTime : expireTimes) {
                if (expireTime > now) {
                    break;
                }
                List<K> keys = new ArrayList<K>(expireTimeIndex.get(expireTime));
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

    @Override
    public Map<K, V> toMap() {
        final Map<K, V> map = new HashMap<K, V>();
        Collects.forEach(this.map, new Consumer2<K, Entry<K, V>>() {
            @Override
            public void accept(K key, Entry<K, V> entry) {
                map.put(key, entry.getValue(false));
            }
        });
        return map;
    }

    @Override
    public void startup() {
        if (!running) {
            running = true;
            computeNextEvictExpiredTime();
            if (evictExpiredInterval > 0) {
                if (timer == null) {
                    timer = new HashedWheelTimer(new CommonThreadFactory("Cache-Evict", false));
                    shutdownTimerSelf = true;
                }
                timer.newTimeout(this.new EvictExpiredTask(), nextEvictExpiredTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public void shutdown() {
        running = false;
        if (timer != null) {
            if (shutdownTimerSelf) {
                timer.stop();
            }
        }
    }

    void setMap(ConcurrentReferenceHashMap<K, Entry<K, V>> map) {
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

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
