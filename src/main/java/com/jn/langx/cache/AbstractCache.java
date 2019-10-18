package com.jn.langx.cache;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.struct.Holder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCache<K, V> implements Cache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCache.class);
    private ConcurrentHashMap<K, Entry<K, V>> map;
    private Loader<K, V> globalLoader;
    // unit: seconds
    private long expireAfterWrite = Long.MAX_VALUE;
    // unit: seconds
    private long expireAfterRead = Long.MAX_VALUE;
    // unit: mills
    private long evictExpiredInterval;
    // unit: mills
    private long nextEvictExpiredTime;
    private RemoveListener<K, V> removeListener;
    private int maxCapacity;
    private float capatityHeightWater = 0.95f;

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
        if (Long.MAX_VALUE - now < evictExpiredInterval) {
            evictExpiredInterval = Long.MAX_VALUE - now;
            nextEvictExpiredTime = Long.MAX_VALUE;
        } else {
            nextEvictExpiredTime = System.currentTimeMillis() + evictExpiredInterval;
        }
    }

    @Override
    public void set(@NonNull K key, @Nullable V value) {
        set(key, value, expireAfterWrite, TimeUnit.SECONDS);
    }

    @Override
    public void set(@NonNull K key, @Nullable V value, long duration, TimeUnit timeUnit) {
        Preconditions.checkTrue(duration >= 0);
        duration = timeUnit.toMillis(duration);
        long now = System.currentTimeMillis();
        long expire = (Long.MAX_VALUE - now <= duration) ? Long.MAX_VALUE : now + duration;
        set(key, value, expire);
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
            remove(key, RemoveCause.REPLACED);
            Entry<K, V> entry = new Entry<K, V>(key, value, expire);
            map.put(key, entry);
            addToCache(entry);
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

    protected abstract void onRead(Entry<K, V> entry, long oldExpireTime);

    private V get(@NonNull K key, @Nullable Supplier<K, V> loader, boolean loadIfAbsent) {
        evictExpired();
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            if (!entry.isExpired()) {
                entry.incrementUseCount();
                long expireTime = entry.getExpireTime();
                if (expireAfterRead > 0) {
                    entry.setExpireTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expireAfterRead));
                }
                onRead(entry, expireTime);
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
            onRead(entry, entry.getExpireTime());
        }
        return value;
    }

    @Override
    public void refresh(@NonNull K key) {
        Preconditions.checkNotNull(key);
        evictExpired();
        Holder<Throwable> exceptionHolder = new Holder<Throwable>();
        V value = loadByGlobalLoader(key, exceptionHolder);
        if (value == null) {
            if (exceptionHolder.get() != null) {
                logger.warn("Error occur when load resource for key: {}, error message: {}, stack:", key, exceptionHolder.get().getMessage(), exceptionHolder.get());
            } else {
                remove(key, RemoveCause.REPLACED);
            }
        } else {
            set(key, value);
        }
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

    protected abstract void removeFromCache(Entry<K, V> entry);

    protected final V remove(@NonNull K key, @NonNull RemoveCause cause) {
        Entry<K, V> entry = map.remove(key);
        V ret = entry == null ? null : entry.getValue();
        if (ret != null) {
            removeFromCache(entry);
            if (removeListener != null) {
                removeListener.onRemove(key, ret, cause);
            }
        }
        return ret;
    }

    @Override
    public V remove(@NonNull K key) {
        evictExpired();
        return remove(key, RemoveCause.EXPLICIT);
    }


    private void evictExpired() {
        if ((evictExpiredInterval >= 0 && System.currentTimeMillis() >= nextEvictExpiredTime) || (map.size() > maxCapacity * capatityHeightWater)) {
            clearExpired();
            Collects.forEach(map, new Consumer2<K, Entry<K, V>>() {
                @Override
                public void accept(K key, Entry<K, V> entry) {
                    entry.incrementAge();
                }
            });
        }
    }

    protected abstract void clearExpired();

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

    void setCapatityHeightWater(float capatityHeightWater) {
        this.capatityHeightWater = capatityHeightWater;
    }
}
