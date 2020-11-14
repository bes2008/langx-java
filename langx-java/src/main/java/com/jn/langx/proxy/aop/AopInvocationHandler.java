package com.jn.langx.proxy.aop;

import com.jn.langx.proxy.SimpleInvocationHandler;

import java.lang.reflect.Method;
import java.util.List;

public class AopInvocationHandler extends SimpleInvocationHandler {
    private MethodInterceptorChainProvider interceptorChainProvider;

    public AopInvocationHandler(Object target, MethodInterceptorChainProvider interceptorChainProvider) {
        super(target);
        this.interceptorChainProvider = interceptorChainProvider;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AopMethodInvocation methodInvocation = new AopMethodInvocation(proxy, target, method, args);
        List<MethodInterceptor> interceptorChain = interceptorChainProvider.get(methodInvocation);
        methodInvocation.setInterceptors(interceptorChain);
        return methodInvocation.proceed();
    }
}
