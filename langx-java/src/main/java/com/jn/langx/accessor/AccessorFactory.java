package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.Factory;

import java.util.List;

/**
 * @since 4.6.10
 */
@SuppressWarnings("rawtypes")
public interface AccessorFactory<T> extends Factory<Class<?>, Accessor<String,T>>{
    @Override
    Accessor<String,T> get(Class<?> klass);

    @SuppressWarnings("rawtypes")
    List<Class> applyTo();

    boolean appliable(Class expectedClazz, Class actualClass);
}
