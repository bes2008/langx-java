package com.jn.langx.proxy.aop;

import com.jn.langx.proxy.ConstructorInvocation;

import java.lang.reflect.Constructor;

public class AopConstructorInvocation implements ConstructorInvocation {
    private Constructor constructor;
    private Object[] arguments;

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Constructor getJoinPoint() {
        return constructor;
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public Object proceed() throws Throwable {
        return null;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
