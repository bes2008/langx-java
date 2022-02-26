package com.jn.langx.registry;

import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.struct.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenericPairRegistry<K, V, P extends Pair<K, V>> extends AbstractInitializable implements Registry<K, P> {
    private Map<K, P> registry;

    public GenericPairRegistry() {
        this(new ConcurrentHashMap<K, P>());
    }

    public GenericPairRegistry(Map<K, P> registry) {
        this.registry = registry;
    }

    @Override
    public void register(P pair) {
        this.registry.put(pair.getKey(), pair);
    }

    @Override
    public void register(K key, P t) {
        this.registry.put(key, t);
    }

    @Override
    public P get(K key) {
        return this.registry.get(key);
    }

    public List<K> keys() {
        return Collects.newArrayList(registry.keySet());
    }

    public List<P> pairs() {
        return Collects.newArrayList(registry.values());
    }

}
