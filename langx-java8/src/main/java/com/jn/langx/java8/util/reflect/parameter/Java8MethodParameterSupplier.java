package com.jn.langx.java8.util.reflect.parameter;

import com.jn.langx.annotation.Name;
import com.jn.langx.util.reflect.parameter.AbstractMethodParameterSupplier;
import com.jn.langx.util.reflect.parameter.MethodParameter;
import com.jn.langx.util.reflect.parameter.ParameterMeta;


@Name("langx_java8")
public class Java8MethodParameterSupplier extends AbstractMethodParameterSupplier {
    @Override
    public MethodParameter get(ParameterMeta meta) {
        init();
        return new Java8MethodParameter(meta);
    }

    @Override
    public boolean usingJdkApi() {
        return true;
    }

}
