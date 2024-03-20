package com.jn.langx.propertyset;

import com.jn.langx.util.BasedStringAccessor;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;

public class PropertySetAccessor extends BasedStringAccessor<String, PropertySet> {
    @Override
    public Object get(String key) {
        return this.getString(key, (String) null);
    }

    @Override
    public String getString(String key, String defaultValue) {
        Preconditions.checkNotEmpty(key, "the property name is null or empty");
        PropertySet propertySource = getTarget();
        if (propertySource.containsProperty(key)) {
            return Objs.toStringOrNull(propertySource.getProperty(key));
        }
        return defaultValue;
    }

    @Override
    public void set(String key, Object value) {
        // ignore it
    }

    @Override
    public void remove(String key) {
        // ignore it
    }
}
