package com.jn.langx.java8.util.reflect.parameter;

import com.jn.langx.util.reflect.parameter.MethodParameter;
import com.jn.langx.util.reflect.parameter.MethodParameterSupplier;
import com.jn.langx.util.reflect.parameter.ParameterMeta;

import java.lang.reflect.Method;

public class Java8MethodParameterSupplier implements MethodParameterSupplier {
    @Override
    public MethodParameter get(ParameterMeta meta) {
        Method method = (Method) meta.getExecutable();
        return new Java8MethodParameter(method.getParameters()[meta.getIndex()]);
    }

    @Override
    public boolean usingJdkApi() {
        return true;
    }
}
