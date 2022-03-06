package com.jn.langx.java8.util.reflect.parameter;

import com.jn.langx.annotation.Name;
import com.jn.langx.util.reflect.parameter.AbstractConstructorParameterSupplier;
import com.jn.langx.util.reflect.parameter.ConstructorParameter;
import com.jn.langx.util.reflect.parameter.ParameterMeta;

@Name("langx_java8")
public class Java8ConstructorParameterSupplier extends AbstractConstructorParameterSupplier {
    @Override
    public ConstructorParameter get(ParameterMeta meta) {
        return new Java8ConstructorParameter(meta);
    }

    @Override
    public boolean usingJdkApi() {
        return true;
    }
}
