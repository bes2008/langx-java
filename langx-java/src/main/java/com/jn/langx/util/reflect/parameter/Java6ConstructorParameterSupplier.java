package com.jn.langx.util.reflect.parameter;

import java.lang.reflect.Constructor;

public class Java6ConstructorParameterSupplier implements ConstructorParameterSupplier {
    @Override
    public boolean usingJdkApi() {
        return false;
    }

    @Override
    public ConstructorParameter get(ParameterMeta meta) {
        return new Java6ConstructorParameter(meta.getName(), meta.getModifiers(), (Constructor) meta.getExecutable(), meta.getIndex());
    }
}
