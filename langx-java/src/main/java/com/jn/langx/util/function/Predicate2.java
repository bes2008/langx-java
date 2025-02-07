package com.jn.langx.util.function;

/**
 * Pre-condition, it is similar to Java 8 Predicate
 *
 * @param <K> the first argument
 * @param <V> the second argument
 */
public interface Predicate2<K, V> {
    /**
     * Tests whether the given pair of arguments satisfy the condition.
     *
     * @param key   the first argument
     * @param value the second argument
     * @return true if the condition is satisfied, otherwise false
     */
    boolean test(K key, V value);
}
