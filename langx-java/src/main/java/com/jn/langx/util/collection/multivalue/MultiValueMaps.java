package com.jn.langx.util.collection.multivalue;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.struct.Pair;

import java.util.Collection;
import java.util.Map;

public class MultiValueMaps {
    public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, V> map) {
        return toMultiValueMap(map, null, null);
    }

    /**
     * @since 2.10.6
     */
    private static <I1, I2, O> Function2<I1, I2, O> createGetParameterMapper(int parameterIndex) {
        parameterIndex = parameterIndex >= 2 ? 1 : parameterIndex;
        parameterIndex = parameterIndex < 0 ? 0 : parameterIndex;
        final int index = parameterIndex;
        return new Function2<I1, I2, O>() {
            @Override
            public O apply(I1 i1, I2 i2) {
                return index == 0 ? (O) i1 : (O) i2;
            }
        };
    }

    /**
     * @since 2.10.6
     */
    public static <K1, V1, K2, V2> MultiValueMap<K2, V2> toMultiValueMap(Map<K1, V1> map, @Nullable final Function2<K1, V1, K2> keyMapper, @Nullable final Function2<K1, V1, V2> valueMapper) {
        final Function2<K1, V1, K2> keyFun = keyMapper == null ? MultiValueMaps.<K1, V1, K2>createGetParameterMapper(0) : keyMapper;
        final Function2<K1, V1, V2> valueFun = valueMapper == null ? MultiValueMaps.<K1, V1, V2>createGetParameterMapper(1) : valueMapper;
        final MultiValueMap<K2, V2> multiValueMap = new LinkedMultiValueMap<K2, V2>();
        Collects.forEach(map, new Consumer2<K1, V1>() {
            @Override
            public void accept(K1 key, V1 value) {
                K2 k2 = keyFun.apply(key, value);
                if (k2 != null) {
                    V2 v2 = valueFun.apply(key, value);
                    multiValueMap.add(k2, v2);
                }
            }
        });
        return multiValueMap;
    }

    /**
     * 将 数组 Iterable<V>中的每一个元素取出来，放到一个  MultiValueMap 中。
     */
    public static <K1, V1, C extends Iterable<V1>, K2, V2> MultiValueMap<K2, V2> toMultiValueMap2(Map<K1, C> map, @Nullable final Function2<K1, V1, K2> keyMapper, @Nullable final Function2<K1, V1, V2> valueMapper) {
        final Function2<K1, V1, K2> keyFun = keyMapper == null ? MultiValueMaps.<K1, V1, K2>createGetParameterMapper(0) : keyMapper;
        final Function2<K1, V1, V2> valueFun = valueMapper == null ? MultiValueMaps.<K1, V1, V2>createGetParameterMapper(1) : valueMapper;
        final MultiValueMap<K2, V2> multiValueMap = new LinkedMultiValueMap<K2, V2>();
        Collects.forEach(map, new Consumer2<K1, C>() {
            @Override
            public void accept(final K1 key, C values) {
                Collects.forEach(values, new Consumer<V1>() {
                    @Override
                    public void accept(V1 value) {
                        K2 k2 = keyFun.apply(key, value);
                        if (k2 != null) {
                            V2 v2 = valueFun.apply(key, value);
                            multiValueMap.add(k2, v2);
                        }
                    }
                });
            }
        });
        return multiValueMap;
    }

    public static <K, V, C extends Iterable<V>> MultiValueMap<K, V> toMultiValueMap2(Map<K, C> map) {
        final MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<K, V>();
        Collects.forEach(map, new Consumer2<K, C>() {
            @Override
            public void accept(final K key, C values) {
                Collects.forEach(values, new Consumer<V>() {
                    @Override
                    public void accept(V value) {
                        multiValueMap.add(key, value);
                    }
                });
            }
        });
        return multiValueMap;
    }

    /**
     * 将 数组 V[]中的每一个元素取出来，放到一个  MultiValueMap 中。
     *
     * @since 2.10.6
     */
    public static <K1, V1, K2, V2> MultiValueMap<K2, V2> toMultiValueMap3(Map<K1, V1[]> map, @Nullable final Function2<K1, V1, K2> keyMapper, @Nullable final Function2<K1, V1, V2> valueMapper) {
        final Function2<K1, V1, K2> keyFun = keyMapper == null ? MultiValueMaps.<K1, V1, K2>createGetParameterMapper(0) : keyMapper;
        final Function2<K1, V1, V2> valueFun = valueMapper == null ? MultiValueMaps.<K1, V1, V2>createGetParameterMapper(1) : valueMapper;
        final MultiValueMap<K2, V2> multiValueMap = new LinkedMultiValueMap<K2, V2>();
        Collects.forEach(map, new Consumer2<K1, V1[]>() {
            @Override
            public void accept(final K1 key, V1[] values) {
                Collects.forEach(values, new Consumer<V1>() {
                    @Override
                    public void accept(V1 value) {
                        K2 k2 = keyFun.apply(key, value);
                        if (k2 != null) {
                            V2 v2 = valueFun.apply(key, value);
                            multiValueMap.add(k2, v2);
                        }
                    }
                });
            }
        });
        return multiValueMap;
    }

    public static <K, V> MultiValueMap<K, V> toMultiValueMap3(Map<K, V[]> map) {
        final MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<K, V>();
        Collects.forEach(map, new Consumer2<K, V[]>() {
            @Override
            public void accept(final K key, V[] values) {
                Collects.forEach(values, new Consumer<V>() {
                    @Override
                    public void accept(V value) {
                        multiValueMap.add(key, value);
                    }
                });
            }
        });
        return multiValueMap;
    }

    public static <K, V, P extends Pair<K, V>, C extends Iterable<P>> MultiValueMap<K, V> toMultiValueMap(C pairs) {
        final MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<K, V>();
        Collects.forEach(pairs, new Consumer<P>() {
            @Override
            public void accept(P pair) {
                multiValueMap.add(pair.getKey(), pair.getValue());
            }
        });
        return multiValueMap;
    }

    public static <K, V, P extends Pair<K, V>> MultiValueMap<K, V> toMultiValueMap(P[] pairs) {
        final MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<K, V>();
        Collects.forEach(pairs, new Consumer<P>() {
            @Override
            public void accept(P pair) {
                multiValueMap.add(pair.getKey(), pair.getValue());
            }
        });
        return multiValueMap;
    }

    public static <K, E> void copy(Map<K, Collection<E>> src, final MultiValueMap<K, E> dest) {
        Collects.forEach(src, new Consumer2<K, Collection<E>>() {
            @Override
            public void accept(K key, Collection<E> value) {
                dest.put(key, value);
            }
        });
    }

    public static <K, E> void copy(MultiValueMap<K, E> src, final Map<K, Collection<E>> dest) {
        Collects.forEach(src, new Consumer2<K, Collection<E>>() {
            @Override
            public void accept(K key, Collection<E> value) {
                dest.put(key, value);
            }
        });
    }

}
