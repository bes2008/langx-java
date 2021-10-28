package com.jn.langx.cache;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.function.Supplier;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface Cache<K, V> {
    /**
     * set a key-value to cache, with out expire time never
     * @param key the key
     * @param value the value
     */
    void set(@NonNull K key, @Nullable V value);

    /**
     * set a key-value to cache, with the specified expire time
     * @param key the key
     * @param value the value
     * @param expireTime the expire time
     */
    void set(@NonNull K key, @Nullable V value, long expireTime);

    /**
     * set a key-value to cache, with the specified expire time
     * @param key the key
     * @param value the value
     * @param ttl the time-to-live time, the live time
     */
    void set(@NonNull K key, @Nullable V value, long ttl, TimeUnit timeUnit);

    /**
     * get by key
     * @param key the key
     * @return the value, null if key is not exist
     */
    V get(@NonNull K key);

    /**
     * multiple get
     * @param keys get the specified keys
     * @return the values
     */
    Map<K, V> getAll(@NonNull Iterable<K> keys);

    /**
     * multiple get
     * @param keys get the specified keys
     * @return the values
     */
    Map<K, V> getAllIfPresent(@NonNull Iterable<K> keys);

    V getIfPresent(@NonNull K key);

    V get(@NonNull K key, @Nullable Supplier<K, V> loader);

    V remove(@NonNull K key);

    List<V> remove(Collection<K> keys);

    void refresh(@NonNull K key);

    /**
     * 刷新所有
     * @param fixed 是否为固定频率的（周期）刷新，若为true，则delay 必须 >0
     * @param delay 延迟时间，delay <=0 代表立即刷新， 大于0则代表延迟刷新
     * @since 4.0.4
     */
    void refreshAllAsync(int delay, boolean fixed);

    /**
     * @since 4.0.4
     */
    void cancelRefreshAll();

    /**
     * @since 4.0.4
     */
    void evictExpired();

    /**
     * @since 4.0.4
     */
    List<K> keys();

    void clean();

    int size();

    Map<K, V> toMap();
}
