package com.jn.langx.proxy.targetdelegate;

import com.jn.langx.proxy.AbstractMethodInvocation;
import com.jn.langx.proxy.SimpleInvocationHandler;

import java.lang.reflect.InvocationTargetException;

/**
 * Use it, the delegate will be the target
 */
public class DelegateInvocationHandler extends SimpleInvocationHandler {
    private final TargetDelegateProvider delegateProvider;

    public DelegateInvocationHandler(Object target, TargetDelegateProvider delegateProvider) {
        super(target);
        this.delegateProvider = delegateProvider;
    }

    @Override
    protected Object execute(AbstractMethodInvocation methodInvocation) throws Throwable {
        try {
            Object delegate = this.delegateProvider.get(methodInvocation);
            Object obj = delegate == null ? target : delegate;
            return methodInvocation.getMethod().invoke(obj, methodInvocation.getArguments());
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
