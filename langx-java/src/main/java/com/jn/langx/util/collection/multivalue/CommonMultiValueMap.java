package com.jn.langx.util.collection.multivalue;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.*;
import com.jn.langx.util.struct.counter.SimpleIntegerCounter;

import java.util.*;

public class CommonMultiValueMap<K, V> implements MultiValueMap<K, V> {
    /**
     * 数据存储结构
     */
    protected Map<K, Collection<V>> targetMap;
    /**
     * 当获取一个不存在的key时，会自动的调用 valuesSupplier 创建一个 Collection
     */
    private Supplier<K, Collection<V>> valuesSupplier;

    public CommonMultiValueMap() {
        this(new HashMap<K, Collection<V>>(), new Supplier<K, Collection<V>>() {
            @Override
            public Collection<V> get(K input) {
                return Collects.emptyArrayList();
            }
        });
    }

    public CommonMultiValueMap(@NonNull Supplier0<Map<K, Collection<V>>> mapSupplier, @NonNull Supplier<K, Collection<V>> valuesSupplier) {
        this(mapSupplier.get(), valuesSupplier);
    }

    public CommonMultiValueMap(@NonNull Map<K, Collection<V>> map, @NonNull Supplier<K, Collection<V>> valuesSupplier) {
        Preconditions.checkNotNull(map);
        Preconditions.checkNotNull(valuesSupplier);
        this.targetMap = map;
        this.valuesSupplier = valuesSupplier;
    }

    private Collection<V> getValues(@NonNull K key) {
        Collection<V> values = targetMap.get(key);
        if (values == null) {
            values = valuesSupplier.get(key);
            targetMap.put(key, values);
        }
        return values;
    }

    @Override
    public V getFirst(K key) {
        if (key == null) {
            return null;
        }
        List<V> vs = get(key);
        if (vs.isEmpty()) {
            return null;
        }
        return vs.get(0);
    }

    @Override
    public void add(K key, V value) {
        if (key == null || value != null) {
            getValues(key).add(value);
        }
    }

    public void addAll(final K key, Collection<? extends V> values) {
        if (key == null) {
            return;
        }
        Collects.forEach(values, new Consumer<V>() {
            @Override
            public void accept(V v) {
                add(key, v);
            }
        });
    }

    @Override
    public void addAll(MultiValueMap<K, V> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        Collects.forEach(map, new Consumer2<K, Collection<V>>() {
            @Override
            public void accept(final K key, Collection<V> values) {
                Collects.forEach(values, new Consumer<V>() {
                    @Override
                    public void accept(V v) {
                        add(key, v);
                    }
                });
            }
        });
    }

    @Override
    public void addIfAbsent(K key, V value) {
        if (key == null || value == null) {
            return;
        }
        if (!containsKey(key)) {
            add(key, value);
        }
    }

    @Override
    public void set(K key, V value) {
        if (key == null || value == null) {
            return;
        }
        Collection<V> values = valuesSupplier.get(key);
        values.add(value);
        this.targetMap.put(key, values);
    }

    @Override
    public void setAll(Map<K, V> values) {
        Collects.forEach(values, new Consumer2<K, V>() {
            @Override
            public void accept(K key, V value) {
                set(key, value);
            }
        });
    }

    @Override
    public Map<K, V> toSingleValueMap() {
        final LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K, V>(this.targetMap.size());
        Collects.forEach(this.targetMap, new Consumer2<K, Collection<V>>() {
            @Override
            public void accept(K key, Collection<V> values) {
                if (Emptys.isNotEmpty(values)) {
                    singleValueMap.put(key, Collects.asList(values).get(0));
                }
            }
        });
        return singleValueMap;
    }

    @Override
    public Map<K, List<V>> toMap() {
        final LinkedHashMap<K, List<V>> map = new LinkedHashMap<K, List<V>>(this.targetMap.size());
        Collects.forEach(this.targetMap, new Consumer2<K, Collection<V>>() {
            @Override
            public void accept(K key, Collection<V> values) {
                if (Emptys.isNotEmpty(values)) {
                    map.put(key, Collects.newArrayList(values));
                }
            }
        });
        return map;
    }

    @Override
    public int size() {
        return this.targetMap.size();
    }

    @Override
    public int total() {
        final SimpleIntegerCounter counter = new SimpleIntegerCounter(0);
        Collects.forEach(this.targetMap, new Consumer2<K, Collection<V>>() {
            @Override
            public void accept(K key, Collection<V> values) {
                counter.increment(values.size());
            }
        });
        return counter.get();
    }

    @Override
    public boolean isEmpty() {
        return total() < 1;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.targetMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return Collects.anyMatch(this.targetMap, new Predicate2<K, Collection<V>>() {
            @Override
            public boolean test(K key, Collection<V> values) {
                return values.contains(value);
            }
        });
    }

    @Override
    public List<V> get(Object key) {
        Collection<V> values = getValues((K) key);
        return Collects.asList(values);
    }

    @Override
    public List<V> remove(Object key) {
        Collection<V> values = this.targetMap.remove(key);
        if (values == null) {
            return null;
        }
        return Collects.asList(values);
    }

    @Override
    public Collection<V> put(final K key, Collection<V> values) {
        Collection<V> old = remove(key);
        addAll(key, values);
        return old;
    }

    @Override
    public void putAll(Map<? extends K, ? extends Collection<V>> map) {
        Collects.forEach(map, new Consumer2<K, Collection<V>>() {
            @Override
            public void accept(K key, Collection<V> values) {
                put(key, values);
            }
        });
    }

    @Override
    public void clear() {
        Collects.forEach(this.targetMap, new Consumer2<K, Collection<V>>() {
            @Override
            public void accept(K key, Collection<V> values) {
                values.clear();
            }
        });
        this.targetMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.targetMap.keySet();
    }

    @Override
    public Collection<Collection<V>> values() {
        return this.targetMap.values();
    }

    @Override
    public Set<Entry<K, Collection<V>>> entrySet() {
        return this.targetMap.entrySet();
    }
}
