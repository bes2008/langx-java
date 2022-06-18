package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.util.collection.Collects;

import java.util.Collection;
import java.util.List;

/**
 * @since 4.6.10
 */
public class StringKeyCollectionAccessorFactory<T> extends AbstractAccessorFactory<T>{
    @Override
    public Accessor<String, T> get(Class<?> klass) {
        return null;
    }

    @Override
    public List<Class> applyTo() {
        return Collects.<Class>asList(Collection.class,Object[].class);
    }

    @Override
    public boolean appliable(Class expectedClazz, Class actualClass) {
        if(actualClass.isArray()){
            return true;
        }
        return super.appliable(expectedClazz, actualClass);
    }
}
