package com.jn.langx.util.reflect.parameter;

import java.lang.reflect.Method;

public class Java6MethodParameterSupplier implements MethodParameterSupplier {
    @Override
    public MethodParameter get(ParameterMeta meta) {
        return new Java6MethodParameter(meta.getName(), meta.getModifiers(), (Method) meta.getExecutable(), meta.getIndex());
    }

    @Override
    public boolean usingJdkApi() {
        return false;
    }
}
