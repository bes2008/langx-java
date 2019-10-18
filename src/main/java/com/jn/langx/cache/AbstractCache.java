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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCache<K, V> implements Cache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCache.class);
    private ConcurrentHashMap<K, Entry<K, V>> map;
    private Loader<K, V> globalLoader;
    private long duration = Long.MAX_VALUE;
    private final long evictExpiredInterval; // mills
    private long nextEvictExpiredTime; // mills
    private RemoveListener<K, V> removeListener;
    private final int maxCapatity;
    private float capatityFactor = 0.95f;

    protected AbstractCache(int maxCapatity, long evictExpiredInterval) {
        this.evictExpiredInterval = evictExpiredInterval;
        Preconditions.checkTrue(evictExpiredInterval >= 0);
        this.maxCapatity = maxCapatity;
        computeNextEvictExpiredTime();
    }

    private void computeNextEvictExpiredTime() {
        nextEvictExpiredTime = System.currentTimeMillis() + evictExpiredInterval;
    }

    @Override
    public void set(@NonNull K key, @Nullable V value) {
        set(key, value, duration);
    }

    @Override
    public void set(@NonNull K key, @Nullable V value, long duration, TimeUnit timeUnit) {
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

    private V get(@NonNull K key, @Nullable Supplier<K, V> loader, boolean loadIfAbsent) {
        evictExpired();
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            if (!entry.isExpired()) {
                entry.incrementUseCount();
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
        if ((evictExpiredInterval >= 0 && System.currentTimeMillis() >= nextEvictExpiredTime) || (map.size() > maxCapatity * capatityFactor)) {
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
}
