package com.jn.langx.invocation.proxy.targetdelegate;

import com.jn.langx.invocation.MethodInvocation;
import com.jn.langx.invocation.SimpleInvocationHandler;

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
    protected Object execute(MethodInvocation methodInvocation) throws Throwable {
        Object delegate = this.delegateProvider.get(methodInvocation);
        Object obj = delegate == null ? target : delegate;
        return methodInvocation.getJoinPoint().invoke(obj, methodInvocation.getArguments());
    }
}
