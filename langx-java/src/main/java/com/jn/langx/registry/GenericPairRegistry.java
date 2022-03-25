package com.jn.langx.registry;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.struct.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenericPairRegistry<K, V, P extends Pair<K, V>> extends AbstractRegistry<K, P> {

    public GenericPairRegistry() {
        this(new ConcurrentHashMap<K, P>());
    }

    public GenericPairRegistry(Map<K, P> registry) {
        super(registry);
    }

    @Override
    public void register(P pair) {
        this.registry.put(pair.getKey(), pair);
    }

    public List<K> keys() {
        return Collects.newArrayList(registry.keySet());
    }

    public List<P> pairs() {
        return Collects.newArrayList(registry.values());
    }

}
