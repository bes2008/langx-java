package com.jn.langx.registry;

import com.jn.langx.Named;
import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.util.collection.Collects;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenericRegistry<T extends Named> extends AbstractInitializable implements Registry<String, T> {
    private Map<String, T> registry;

    public GenericRegistry() {
        this(new ConcurrentHashMap<String, T>());
    }

    public GenericRegistry(Map<String, T> registry) {
        this.registry = registry;
    }

    @Override
    public void register(T t) {
        this.registry.put(t.getName(), t);
    }

    @Override
    public void register(String key, T t) {
        this.registry.put(key, t);
    }

    @Override
    public T get(String name) {
        return this.registry.get(name);
    }

    public List<String> names() {
        return Collects.newArrayList(registry.keySet());
    }

    public List<T> instances(){
        return Collects.newArrayList(registry.values());
    }

}
