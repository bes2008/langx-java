package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.MapAccessor;

import java.util.List;
import java.util.Map;

public class StringKeyMapAccessorFactory<T> extends AbstractAccessorFactory<T> {
    @Override
    public Accessor<String, T> get(Class<?> klass) {
        Accessor accessor = new MapAccessor();
        return accessor;
    }

    @Override
    public List<Class> applyTo() {
        return Collects.<Class>asList(Map.class);
    }
}
