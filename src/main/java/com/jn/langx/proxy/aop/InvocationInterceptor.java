package com.jn.langx.proxy.aop;

public interface InvocationInterceptor<I extends Invocation> {
    Object intercept(I invocation) throws Throwable;
}
