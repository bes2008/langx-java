package com.jn.langx.environment;

import com.jn.langx.propertyset.PropertySet;

/**
 * @since 5.4.6
 */
public interface CompoundEnvironment extends Environment{
    PropertySet getPropertySet(String name);
}
