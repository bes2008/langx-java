package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.struct.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentMap;


public class Maps {
    private Maps(){

    }
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

    public static <K, V> V get(@NonNull Map<K, V> map, @NonNull K key, Supplier<K, V> defaultValueSupplier) {
        V v = map.get(key);
        if (v == null) {
            if (defaultValueSupplier != null) {
                v = defaultValueSupplier.get(key);
            }
        }
        return v;
    }

    public static <K, V> V get(@NonNull Map<K, V> map, @NonNull K key, Supplier0<V> defaultValueSupplier) {
        V v = map.get(key);
        if (v == null) {
            if (defaultValueSupplier != null) {
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

    public static <K, V> V replace(final @NonNull Map<K, V> map, @NonNull K key, @NonNull V value) {
        return replace(map, key, value, null);
    }

    public static <K, V> V replace(final @NonNull Map<K, V> map, @NonNull K key, @NonNull V value, Predicate2<K, V> predicate) {
        V currentValue = map.get(key);
        if (predicate == null) {
            predicate = new Predicate2<K, V>() {
                @Override
                public boolean test(K key, V value) {
                    return map.containsKey(key) || value != null;
                }
            };
        }
        if (predicate.test(key, currentValue)) {
            map.put(key, value);
        }
        return currentValue;
    }

    @NonNull
    public static <K, V> Map<K, V> of() {
        return of();
    }

    @NonNull
    public static <K, V> Map<K, V> of(K k, V v) {
        return newMap(p(k, v));
    }

    @NonNull
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        return newMap(p(k1, v1), p(k2, v2));
    }

    @NonNull
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return newMap(p(k1, v1), p(k2, v2), p(k3, v3));
    }

    @NonNull
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return newMap(p(k1, v1), p(k2, v2), p(k3, v3), p(k4, v4));
    }

    @NonNull
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return newMap(p(k1, v1), p(k2, v2), p(k3, v3), p(k4, v4), p(k5, v5));
    }

    private static <K, V> Pair<K, V> p(K k, V v) {
        return new Pair(k, v);
    }

    public static <K, V> Map<K, V> newMap(Pair<K, V>... pairs) {
        Map<K, V> map = new HashMap<K, V>(pairs.length);
        for (int i = 0; i < pairs.length; ++i) {
            Pair<K, V> pair = pairs[i];
            map.put(pair.getKey(), pair.getValue());
        }

        return map;
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return Collects.newHashMap();
    }

    public static <K, V> HashMap<K, V> newHashMap(Map<K, V> map) {
        return Collects.newHashMap(map);
    }

    public static <K, V> HashMap<K, V> newHashMap(int expectedSize) {
        return Maps.newHashMapWithExpectedSize(expectedSize);
    }

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return Collects.newHashMapWithExpectedSize(expectedSize);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return Collects.newLinkedHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<K, V> map) {
        return Collects.newLinkedHashMap(map);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
        return Collects.newLinkedHashMapWithExpectedSize(expectedSize);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return Collects.newConcurrentMap();
    }

    public static <K, V> TreeMap<K, V> newTreeMap() {
        return Collects.newTreeMap();
    }

    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, V> map) {
        return Collects.newTreeMap(map);
    }

    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(Comparator<C> comparator) {
        return Collects.newTreeMap(comparator);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
        return Collects.newEnumMap(type);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, V> map) {
        return Collects.newEnumMap(map);
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return Collects.newIdentityHashMap();
    }

}
