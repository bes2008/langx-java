package com.jn.langx.invocation.aop;

import com.jn.langx.invocation.Invocation;

public interface InvocationInterceptor<I extends Invocation> {
    Object intercept(I invocation) throws Throwable;
}
