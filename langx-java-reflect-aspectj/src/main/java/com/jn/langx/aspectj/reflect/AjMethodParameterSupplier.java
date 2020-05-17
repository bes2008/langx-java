package com.jn.langx.aspectj.reflect;

import com.jn.langx.annotation.Name;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.reflect.ParameterServiceRegistry;
import com.jn.langx.util.reflect.parameter.AbstractMethodParameterSupplier;
import com.jn.langx.util.reflect.parameter.MethodParameter;
import com.jn.langx.util.reflect.parameter.MethodParameterSupplier;
import com.jn.langx.util.reflect.parameter.ParameterMeta;

@Name("langx_aspectj")
public class AjMethodParameterSupplier extends AbstractMethodParameterSupplier {
    private MethodParameterSupplier delegate;

    public AjMethodParameterSupplier() {

    }

    public AjMethodParameterSupplier(MethodParameterSupplier delegate) {
        this.delegate = delegate;
    }

    @Override
    public void init() throws InitializationException {
        if (!inited) {
            if (delegate == null) {
                delegate = ParameterServiceRegistry.getInstance().getDefaultMethodParameterSupplier();
            }
            inited = true;
        }
    }

    @Override
    public boolean usingJdkApi() {
        return false;
    }

    @Override
    public MethodParameter get(ParameterMeta meta) {
        init();
        MethodParameter delegate = this.delegate.get(meta);
        return new AjMethodParameter(delegate);
    }
}
