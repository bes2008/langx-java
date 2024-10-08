package com.jn.langx.environment;

import com.jn.langx.util.BasedStringAccessor;

public class EnvironmentAccessor extends BasedStringAccessor<String, Environment> {

    @Override
    public Object get(String key) {
        return getTarget().getProperty(key);
    }

    @Override
    public boolean has(String key) {
        return getTarget().getProperty(key) != null;
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getTarget().getProperty(key, defaultValue);
    }

    @Override
    public void set(String key, Object value) {

    }

    @Override
    public void remove(String key) {

    }
}
