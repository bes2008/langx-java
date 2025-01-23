package com.jn.langx.environment;

import com.jn.langx.propertyset.PropertySet;

public interface Environment {
    String getProperty(String key);

    String getProperty(String key, String valueIfAbsent);

    /**
     * @since 5.4.6
     */
    PropertySet getPropertySet(String name);
}
