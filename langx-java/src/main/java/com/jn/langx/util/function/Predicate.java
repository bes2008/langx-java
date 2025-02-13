package com.jn.langx.util.function;

/**
 * Pre-condition, it is similar to Java 8 Predicate
 *
 * @param <V> the argument
 */
public interface Predicate<V> {
    /**
     * Determines if the given argument meets the condition.
     *
     * @param value the value to be tested
     * @return true if the argument meets the condition; otherwise, false.
     */
    boolean test(V value);
}
