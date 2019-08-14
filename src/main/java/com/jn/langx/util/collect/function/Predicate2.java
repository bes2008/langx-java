package com.jn.langx.util.collect.function;

/**
 * Pre-condition, it is similar to Java 8 Predicate
 * @param <K> the first argument
 * @param <V> the second argument
 */
public interface Predicate2<K, V> {
    boolean test(K key, V value);
}
