package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Supplier;

import java.util.Map;

public class Maps {

    public static <K,V> V putIfAbsent(@NonNull Map<K, V> map, @NonNull K key, final V value){
        if(value instanceof Supplier){
            final Supplier<K,V> supplier = (Supplier<K, V>)value;
            putIfAbsent(map, key,new Function<K, V>() {
                @Override
                public V apply(K key) {
                    return supplier.get(key);
                }
            });
        }
        return putIfAbsent(map, key, new Function<K, V>() {
            @Override
            public V apply(K key) {
                return value;
            }
        });
    }

    public static <K, V> V putIfAbsent(@NonNull Map<K, V> map, @NonNull K key, @NonNull Function<K, V> mapper) {
        V v = map.get(key);
        if (v == null) {
            v = mapper.apply(key);
            if (v != null) {
                map.put(key, v);
            }
        }
        return v;
    }

    public static <K, V> V putIfAbsent(@NonNull Map<K, V> map, @NonNull K key, final Supplier<K, V> supplier) {
        return putIfAbsent(map, key, new Function<K, V>() {
            public V apply(K key) {
                return supplier.get(key);
            }
        });
    }
}
