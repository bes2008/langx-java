package com.jn.langx.invocation;

import com.jn.langx.util.collection.Arrs;

import java.lang.reflect.Method;

public class GenericMethodInvocation implements MethodInvocation {
    private Method method;
    private Object target;
    private Object[] arguments;
    private Object proxy;

    public GenericMethodInvocation(Object proxy, Object target, Method method, Object[] arguments) {
       setArguments(arguments);
        this.proxy = proxy;
        this.target = target;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object[] getArguments() {
        return Arrs.copy(arguments);
    }

    public void setArguments(Object[] arguments) {
        this.arguments = Arrs.copy(arguments);
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    @Override
    public Method getJoinPoint() {
        return getMethod();
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public Object proceed() throws Throwable {
        return getJoinPoint().invoke(getThis(), getArguments());
    }
}
