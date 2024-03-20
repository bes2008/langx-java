package com.jn.langx.propertyset;

import java.util.Map;
/**
 * @since 5.3.8
 */
public class MapPropertySet extends AbstractPropertySet<Map<String,?>> {
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
    public boolean containsProperty(String key) {
        return getSource().containsKey(key);
    }
}
