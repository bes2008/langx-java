package com.jn.langx.proxy.aop;

import com.jn.langx.proxy.Invocation;

public interface InvocationInterceptor<I extends Invocation> {
    Object intercept(I invocation) throws Throwable;
}
