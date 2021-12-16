package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.function.Supplier0;

import java.util.Map;

public class Maps {

    public static <K, V> V get(@NonNull Map<K, V> map, @NonNull K key) {
        return map.get(key);
    }

    public static <K, V> V get(@NonNull Map<K, V> map, @NonNull K key, V defaultValue) {
        V v = map.get(key);
        if (v == null) {
            v = defaultValue;
        }
        return v;
    }

    public static <K, V> V get(@NonNull Map<K, V> map, @NonNull K key, Supplier<K,V> defaultValueSupplier) {
        V v = map.get(key);
        if (v == null) {
            if(defaultValueSupplier!=null){
                v = defaultValueSupplier.get(key);
            }
        }
        return v;
    }

    public static <K, V> V get(@NonNull Map<K, V> map, @NonNull K key, Supplier0<V> defaultValueSupplier) {
        V v = map.get(key);
        if (v == null) {
            if(defaultValueSupplier!=null){
                v = defaultValueSupplier.get();
            }
        }
        return v;
    }



    public static <K, V> V putIfAbsent(@NonNull Map<K, V> map, @NonNull K key, final V value) {
        // jdk 8 中，value 为 supplier时，不会走参数有supplier的方法，很纳闷
        if (value instanceof Supplier) {
            final Supplier<K, V> supplier = (Supplier<K, V>) value;
            putIfAbsent(map, key, new Function<K, V>() {
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

    public static <K, V> V putIfAbsent(@NonNull Map<K, V> map, @NonNull K key, @NonNull Function<K, V> valueFactory) {
        V v = map.get(key);
        if (v == null) {
            v = valueFactory.apply(key);
            if (v != null) {
                map.put(key, v);
            }
        }
        return v;
    }

    public static <K, V> V putIfAbsent(@NonNull Map<K, V> map, @NonNull K key, final Supplier<K, V> valueSupplier) {
        return putIfAbsent(map, key, new Function<K, V>() {
            public V apply(K key) {
                return valueSupplier.get(key);
            }
        });
    }

    public static <K,V> V replace(final @NonNull Map<K, V> map, @NonNull K key, @NonNull V value){
        return replace(map,key,value, null);
    }

    public static <K,V> V replace(final @NonNull Map<K, V> map, @NonNull K key, @NonNull V value, Predicate2<K,V> predicate){
        V currentValue = map.get(key);
        if(predicate==null){
            predicate = new Predicate2<K, V>() {
                @Override
                public boolean test(K key, V value) {
                    return map.containsKey(key) || value!=null;
                }
            };
        }
        if(predicate.test(key, currentValue)){
            map.put(key, value);
        }
        return currentValue;
    }
}
