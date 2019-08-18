package com.jn.langx.proxy.delegate;

import com.jn.langx.proxy.MethodInvocation;
import com.jn.langx.proxy.SimpleInvocationHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DelegateInvocationHandler extends SimpleInvocationHandler {
    private final DelegateProvider delegateProvider;

    public DelegateInvocationHandler(Object target, DelegateProvider delegateProvider) {
        super(target);
        this.delegateProvider = delegateProvider;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            MethodInvocation methodInvocation = new MethodInvocation(proxy, null, method, args);
            Object delegate = this.delegateProvider.get(methodInvocation);
            Object obj = delegate == null ? target : delegate;
            return method.invoke(obj, args);
        } catch (InvocationTargetException e) {

            throw e.getTargetException();
        }
    }
}
