package com.jn.langx.aspectj.reflect;

import com.jn.langx.annotation.Name;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.reflect.ParameterServiceRegistry;
import com.jn.langx.util.reflect.parameter.AbstractConstructorParameterSupplier;
import com.jn.langx.util.reflect.parameter.ConstructorParameter;
import com.jn.langx.util.reflect.parameter.ConstructorParameterSupplier;
import com.jn.langx.util.reflect.parameter.ParameterMeta;

@Name("langx_aspectj")
public class AjConstructorParameterSupplier extends AbstractConstructorParameterSupplier {
    private ConstructorParameterSupplier delegate;

    public AjConstructorParameterSupplier() {
    }

    public AjConstructorParameterSupplier(ConstructorParameterSupplier delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean usingJdkApi() {
        return false;
    }

    @Override
    public void init() throws InitializationException {
        if (!inited) {
            if (delegate == null) {
                delegate = ParameterServiceRegistry.getInstance().getDefaultConstructorParameterSupplier();
            }
            inited = true;
        }
    }

    @Override
    public ConstructorParameter get(ParameterMeta input) {
        init();
        return null;
    }
}
