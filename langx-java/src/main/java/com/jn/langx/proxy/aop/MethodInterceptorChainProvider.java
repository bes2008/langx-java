package com.jn.langx.proxy.aop;

import com.jn.langx.factory.Provider;
import com.jn.langx.proxy.MethodInvocation;

import java.util.List;

public interface MethodInterceptorChainProvider extends Provider<MethodInvocation, List<MethodInterceptor>> {
}
