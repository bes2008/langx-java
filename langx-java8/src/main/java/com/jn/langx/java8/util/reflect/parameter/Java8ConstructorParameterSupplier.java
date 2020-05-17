package com.jn.langx.java8.util.reflect.parameter;

import com.jn.langx.annotation.Name;
import com.jn.langx.util.reflect.parameter.ConstructorParameter;
import com.jn.langx.util.reflect.parameter.ConstructorParameterSupplier;
import com.jn.langx.util.reflect.parameter.ParameterMeta;

import java.lang.reflect.Constructor;

@Name("langx_java8")
public class Java8ConstructorParameterSupplier implements ConstructorParameterSupplier {
    @Override
    public ConstructorParameter get(ParameterMeta meta) {
        Constructor constructor = (Constructor) meta.getExecutable();
        return new Java8ConstructorParameter(constructor.getParameters()[meta.getIndex()]);
    }

    @Override
    public boolean usingJdkApi() {
        return true;
    }
}
