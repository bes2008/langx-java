package com.jn.langx.registry;

import com.jn.langx.Factory;

/**
 * The Registry interface represents a registry system, extending the functionality of the Factory interface.
 * It allows for the registration, unregistration, and query of existence of elements.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public interface Registry<K, V> extends Factory<K, V> {
    /**
     * Registers an element into the registry. The specific registration logic depends on the implementation class.
     *
     * @param v the element to register
     */
    void register(V v);

    /**
     * Registers an element with a specified key into the registry. Allows for quick retrieval of the element using the key.
     *
     * @param key the key for registration
     * @param v   the element to register
     */
    void register(K key, V v);

    /**
     * Unregisters an element from the registry using its key.
     *
     * @param key the key of the element to unregister
     * @since 4.4.2
     */
    void unregister(K key);

    /**
     * Checks if the registry contains an element with the specified key.
     *
     * @param key the key to check
     * @return true if the registry contains an element with the specified key; otherwise, false.
     * @since 4.4.2
     */
    boolean contains(K key);
}
