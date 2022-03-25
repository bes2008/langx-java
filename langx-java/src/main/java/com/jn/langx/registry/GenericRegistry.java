package com.jn.langx.registry;

import com.jn.langx.Named;
import com.jn.langx.util.collection.Collects;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenericRegistry<T extends Named> extends AbstractRegistry<String, T> {

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

    public List<String> names() {
        return Collects.newArrayList(registry.keySet());
    }

    public List<T> instances(){
        return Collects.newArrayList(registry.values());
    }

}
