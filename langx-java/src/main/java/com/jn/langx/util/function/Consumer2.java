package com.jn.langx.util.function;

/**
 * Consume a data, it is similar to Java 8 Consumer.
 * It has two arguments
 *
 * @see Consumer
 */
public interface Consumer2<K, V> {
    void accept(K key, V value);
}
