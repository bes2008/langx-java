package com.jn.langx.util.collection.multivalue;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.struct.Pair;

import java.util.Map;

public class MultiValueMaps {
    public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, V> map) {
        final MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<K, V>();
        Collects.forEach(map, new Consumer2<K, V>() {
            @Override
            public void accept(K key, V value) {
                multiValueMap.add(key, value);
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

    public static <K,V,P extends Pair<K,V>,C extends Iterable<P>> MultiValueMap<K,V> toMultiValueMap(C pairs){
        final MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<K, V>();
        Collects.forEach(pairs, new Consumer<P>() {
            @Override
            public void accept(P pair) {
                multiValueMap.add(pair.getKey(),pair.getValue());
            }
        });
        return multiValueMap;
    }

    public static <K,V,P extends Pair<K,V>> MultiValueMap<K,V> toMultiValueMap(P[] pairs){
        final MultiValueMap<K, V> multiValueMap = new LinkedMultiValueMap<K, V>();
        Collects.forEach(pairs, new Consumer<P>() {
            @Override
            public void accept(P pair) {
                multiValueMap.add(pair.getKey(),pair.getValue());
            }
        });
        return multiValueMap;
    }
}
