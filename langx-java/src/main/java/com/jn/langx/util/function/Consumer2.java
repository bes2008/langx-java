package com.jn.langx.util.function;

/**
 * Consume a data, it is similar to Java 8 Consumer.
 * It has two arguments
 *
 * @see Consumer
 */
public interface Consumer2<K, V> {
    /**
     * Performs the operation on the given key and value.
     *
     * @param key the key to operate on, not null
     * @param value the value to operate on, not null
     */
    void accept(K key, V value);
}
