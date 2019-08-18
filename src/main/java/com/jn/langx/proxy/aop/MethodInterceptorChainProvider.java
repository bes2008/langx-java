package com.jn.langx.proxy.aop;

import com.jn.langx.factory.Provider;
import com.jn.langx.proxy.MethodInvocation;

import java.util.List;

public class MethodInterceptorChainProvider implements Provider<com.jn.langx.proxy.MethodInvocation, List<MethodInterceptor>> {
    @Override
    public List<MethodInterceptor> get(MethodInvocation input) {
        return null;
    }
}
