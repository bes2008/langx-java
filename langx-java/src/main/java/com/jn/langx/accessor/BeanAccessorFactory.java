package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.FieldAccessor;

import java.util.List;

/**
 * @since 4.6.10
 */
public class BeanAccessorFactory<T> extends AbstractAccessorFactory<T> {
    @Override
    public Accessor<String, T> get(Class<?> klass) {
        Accessor accessor = new FieldAccessor();
        return accessor;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Class> applyTo() {
        return Collects.<Class>asList(Object.class);
    }
}
