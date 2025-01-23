package com.jn.langx.environment;

import com.jn.langx.propertyset.MultiplePropertySet;
import com.jn.langx.propertyset.PropertySet;
import com.jn.langx.util.Objs;

import java.util.List;

/**
 * @since 5.3.8
 */
public class MultiplePropertySetEnvironment implements CompoundEnvironment {
    private MultiplePropertySet propertySet;

    public MultiplePropertySetEnvironment(String name, List<PropertySet> propertySets) {
       this(new MultiplePropertySet(name, propertySets));
    }

    public MultiplePropertySetEnvironment(MultiplePropertySet propertySet) {
        this.propertySet=propertySet;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key,null);
    }

    @Override
    public String getProperty(String key, String valueIfAbsent) {
        Object value = this.propertySet.getProperty(key);
        value = Objs.useValueIfNull(value, valueIfAbsent);
        return Objs.toStringOrNull(value);
    }

    /**
     * @since 5.4.6
     */
    @Override
    public PropertySet getPropertySet(String name) {
        return this.propertySet.getPropertySet(name);
    }
}
