package com.jn.langx.util.collection;

import com.jn.langx.util.BasedStringAccessor;

import java.util.Map;

public class MapAccessor extends BasedStringAccessor<String, Map<String, Object>> {
    public MapAccessor() {
    }

    public MapAccessor(Map<String, Object> target) {
        this();
        setTarget(target);
    }

    @Override
    public Object get(String key) {
        return getTarget().get(key);
    }

    @Override
    public boolean has(String key) {
        return getTarget().containsKey(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        Object value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    @Override
    public void set(String key, Object value) {
        getTarget().put(key, value);
    }

    @Override
    public void remove(String key) {
        getTarget().remove(key);
    }
}
