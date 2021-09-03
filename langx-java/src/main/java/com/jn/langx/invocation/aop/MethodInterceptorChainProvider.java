package com.jn.langx.invocation.aop;

import com.jn.langx.Provider;
import com.jn.langx.invocation.MethodInvocation;

import java.util.List;

public interface MethodInterceptorChainProvider extends Provider<MethodInvocation, List<MethodInterceptor>> {
    @Override
    List<MethodInterceptor> get(MethodInvocation invocation);
}
