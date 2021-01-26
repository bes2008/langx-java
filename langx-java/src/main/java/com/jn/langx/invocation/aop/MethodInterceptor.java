package com.jn.langx.invocation.aop;

import com.jn.langx.invocation.MethodInvocation;

public interface MethodInterceptor extends InvocationInterceptor<MethodInvocation> {
    @Override
    Object intercept(MethodInvocation invocation) throws Throwable;
}
