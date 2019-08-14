package com.jn.langx.util.collect.function;

/**
 * Pre-condition, it is similar to Java 8 Predicate
 * @param <V> the argument
 */
public interface Predicate<V> {
    boolean test(V value);
}
