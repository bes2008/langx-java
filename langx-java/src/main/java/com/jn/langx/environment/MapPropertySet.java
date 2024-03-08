package com.jn.langx.environment;

import java.util.Map;

public class MapPropertySet extends AbstractPropertySet<Map<String,?>>{
    public MapPropertySet(String name) {
        super(name);
    }

    public MapPropertySet(String name, Map<String, ?> source) {
        super(name, source);
    }

    @Override
    public Object getProperty(String key) {
        return getSource().get(key);
    }

    @Override
    public boolean hasProperty(String key) {
        return getSource().containsKey(key);
    }
}
