package com.jn.langx.util.reflect.parameter;

import com.jn.langx.annotation.Name;

import java.lang.reflect.Constructor;

@Name("langx_java6")
public class Java6ConstructorParameterSupplier extends AbstractConstructorParameterSupplier {
    @Override
    public boolean usingJdkApi() {
        return false;
    }

    @Override
    public ConstructorParameter get(ParameterMeta meta) {
        return new Java6ConstructorParameter(meta.getName(), meta.getModifiers(), (Constructor) meta.getExecutable(), meta.getIndex());
    }
}
