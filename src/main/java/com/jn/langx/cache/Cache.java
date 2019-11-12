package com.jn.langx.cache;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.function.Supplier;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface Cache<K, V> {
    void set(@NonNull K key, @Nullable V value);

    void set(@NonNull K key, @Nullable V value, long expire);

    void set(@NonNull K key, @Nullable V value, long expire, TimeUnit timeUnit);

    V get(@NonNull K key);

    V getIfPresent(@NonNull K key);

    V get(@NonNull K key, @Nullable Supplier<K, V> loader);

    V remove(@NonNull K key);

    void refresh(@NonNull K key);

    void clean();

    int size();

    Map<K, V> toMap();
}
