package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.ArrayAccessor;

import java.util.List;

/**
 * @since 4.6.10
 */
public class ArrayAccessorFactory implements AccessorFactory<ArrayAccessor> {
    @Override
    public Accessor<String, ArrayAccessor> get(Class<?> klass) {
        return new ArrayAccessor();
    }

    @Override
    public List<Class> applyTo() {
        return Collects.<Class>newArrayList(Object[].class);
    }

    @Override
    public boolean appliable(Class expectedClazz, Class actualClass) {
        return actualClass.isArray();
    }
}
