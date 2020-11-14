package com.jn.langx.invocation.aop;

import com.jn.langx.factory.Provider;
import com.jn.langx.invocation.MethodInvocation;

import java.util.List;

public interface MethodInterceptorChainProvider extends Provider<MethodInvocation, List<MethodInterceptor>> {
}
