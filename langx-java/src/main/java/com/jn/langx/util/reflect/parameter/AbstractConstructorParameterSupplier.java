package com.jn.langx.util.reflect.parameter;

import com.jn.langx.lifecycle.InitializationException;

public abstract class AbstractConstructorParameterSupplier implements ConstructorParameterSupplier {
    protected volatile boolean inited = false;

    @Override
    public void init() throws InitializationException {
        if (!inited) {
            inited = true;
        }
    }
}
