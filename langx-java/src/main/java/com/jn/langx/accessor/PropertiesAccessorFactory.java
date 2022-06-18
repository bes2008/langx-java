package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.text.properties.PropertiesAccessor;
import com.jn.langx.util.collection.Collects;

import java.util.List;
import java.util.Properties;

/**
 * @since 4.6.10
 */
public class PropertiesAccessorFactory<T> extends AbstractAccessorFactory<T> {
    @Override
    public Accessor<String, T> get(Class<?> klass) {
        Accessor accessor = new PropertiesAccessor();
        return accessor;
    }

    @Override
    public List<Class> applyTo() {
        return Collects.<Class>asList(Properties.class);
    }
}