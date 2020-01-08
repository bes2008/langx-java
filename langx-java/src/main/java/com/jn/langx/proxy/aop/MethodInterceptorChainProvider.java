package com.jn.langx.proxy.aop;

import com.jn.langx.factory.Provider;

import java.util.List;

public interface MethodInterceptorChainProvider extends Provider<com.jn.langx.proxy.MethodInvocation, List<MethodInterceptor>> {
}
