package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.Factory;

public interface AccessorFactory extends Factory<Class<?>, Accessor<String,?>> {
    @Override
    Accessor<String,?> get(Class<?> klass);
}
