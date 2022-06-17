package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.Factory;

import java.util.List;

public interface AccessorFactory<T> extends Factory<Class<?>, Accessor<String,T>>{
    @Override
    Accessor<String,T> get(Class<?> klass);

    List<Class> applyTo();
}
