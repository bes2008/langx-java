package com.jn.langx.environment;

import com.jn.langx.propertyset.MultiplePropertySet;
import com.jn.langx.propertyset.PropertySet;
import com.jn.langx.util.Objs;

import java.util.List;

public class MultiplePropertySetEnvironment implements Environment {
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
}
