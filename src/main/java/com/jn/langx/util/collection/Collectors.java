package com.jn.langx.util.collection;

import com.jn.langx.util.function.Collector;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Supplier0;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Collectors {
    public static <E> Collector<E, Set<E>> toTreeSet(final Comparator<E> comparator) {
        return new Collector<E, Set<E>>() {
            @Override
            public Supplier0<Set<E>> supplier() {
                return new Supplier0<Set<E>>() {
                    @Override
                    public Set<E> get() {
                        return Collects.emptyTreeSet(comparator);
                    }
                };
            }

            @Override
            public Consumer2<Set<E>, E> accumulator() {
                return new Consumer2<Set<E>, E>() {
                    @Override
                    public void accept(Set<E> set, E value) {
                        set.add(value);
                    }
                };
            }
        };
    }

    public static <E> Collector<E, Set<E>> toHashSet(final boolean sequential) {
        return new Collector<E, Set<E>>() {
            @Override
            public Supplier0<Set<E>> supplier() {
                return new Supplier0<Set<E>>() {
                    @Override
                    public Set<E> get() {
                        return Collects.emptyHashSet(sequential);
                    }
                };
            }

            @Override
            public Consumer2<Set<E>, E> accumulator() {
                return new Consumer2<Set<E>, E>() {
                    @Override
                    public void accept(Set<E> set, E value) {
                        set.add(value);
                    }
                };
            }
        };
    }

    public static <E> Collector<E, List<E>> toList() {
        return new Collector<E, List<E>>() {
            @Override
            public Supplier0<List<E>> supplier() {
                return new Supplier0<List<E>>() {
                    @Override
                    public List<E> get() {
                        return Collects.emptyArrayList();
                    }
                };
            }

            @Override
            public Consumer2<List<E>, E> accumulator() {
                return new Consumer2<List<E>, E>() {
                    @Override
                    public void accept(List<E> list, E value) {
                        list.add(value);
                    }
                };
            }
        };
    }

    public static <E, K, V> Collector<E, Map<K, V>> toHashMap(final Function<E, K> keyMapper, final Function<E, V> valueMapper, final boolean sequential) {
        return new Collector<E, Map<K, V>>() {
            @Override
            public Supplier0<Map<K, V>> supplier() {
                return new Supplier0<Map<K, V>>() {
                    @Override
                    public Map<K, V> get() {
                        return Collects.emptyHashMap(sequential);
                    }
                };
            }

            @Override
            public Consumer2<Map<K, V>, E> accumulator() {
                return new Consumer2<Map<K, V>, E>() {
                    @Override
                    public void accept(Map<K, V> map, E e) {
                        K key = keyMapper.apply(e);
                        V value = valueMapper.apply(e);
                        map.put(key, value);
                    }
                };
            }
        };
    }

    public static <E, K, V> Collector<E, Map<K, V>> toTreeMap(final Function<E, K> keyMapper, final Function<E, V> valueMapper, final Comparator<K> comparator) {
        return new Collector<E, Map<K, V>>() {
            @Override
            public Supplier0<Map<K, V>> supplier() {
                return new Supplier0<Map<K, V>>() {
                    @Override
                    public Map<K, V> get() {
                        return Collects.emptyTreeMap(comparator);
                    }
                };
            }

            @Override
            public Consumer2<Map<K, V>, E> accumulator() {
                return new Consumer2<Map<K, V>, E>() {
                    @Override
                    public void accept(Map<K, V> map, E e) {
                        K key = keyMapper.apply(e);
                        V value = valueMapper.apply(e);
                        map.put(key, value);
                    }
                };
            }
        };
    }

}
