package com.jn.langx.util.collection;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Supplier;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class NonAbsentTreeMap<K, V> extends TreeMap<K, V> {
    private Supplier<K, V> supplier;

    public NonAbsentTreeMap(@NonNull Supplier<K, V> supplier) {
        super();
        setSupplier(supplier);
    }

    public NonAbsentTreeMap(Comparator<? super K> comparator, @NonNull Supplier<K, V> supplier) {
        super(comparator);
        setSupplier(supplier);
    }

    public NonAbsentTreeMap(Map<? extends K, ? extends V> m, @NonNull Supplier<K, V> supplier) {
        super(m);
        setSupplier(supplier);
    }

    public NonAbsentTreeMap(SortedMap<K, ? extends V> m, @NonNull Supplier<K, V> supplier) {
        super(m);
        setSupplier(supplier);
    }

    private void setSupplier(Supplier<K, V> supplier) {
        Preconditions.checkNotNull(supplier);
        this.supplier = supplier;
    }

    public V get(Object key, Supplier<K, V> supplier) {
        V v = getIfPresent(key);
        if (v == null) {
            supplier = supplier != null ? supplier : this.supplier;
            v = putIfAbsent((K) key, supplier.get((K) key));
        }
        return v;
    }

    @Override
    public V get(Object key) {
        return get(key, null);
    }

    public V getIfPresent(Object key) {
        return super.get(key);
    }

    public V putIfAbsent(K key, V value) {
        V v = super.get(key);
        if (v == null) {
            super.put(key, value);
            v = value;
        }
        return v;
    }
}
