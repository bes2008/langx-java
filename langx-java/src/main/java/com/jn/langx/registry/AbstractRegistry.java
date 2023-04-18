package com.jn.langx.registry;

import com.jn.langx.lifecycle.AbstractInitializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since 4.4.2
 */
public abstract class AbstractRegistry<K,V> extends AbstractInitializable implements Registry<K,V>{
    protected Map<K, V> registry;

    protected AbstractRegistry() {
        this(new ConcurrentHashMap<K, V>());
    }

    protected AbstractRegistry(Map<K, V> registry) {
        this.registry = registry;
    }

    @Override
    public V get(K key) {
        return registry.get(key);
    }


    @Override
    public void register(K key, V v) {
        registry.put(key,v);
    }

    @Override
    public void unregister(K key) {
        registry.remove(key);
    }

    @Override
    public boolean contains(K key) {
        return registry.containsKey(key);
    }


}
