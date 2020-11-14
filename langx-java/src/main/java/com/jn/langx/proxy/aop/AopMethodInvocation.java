package com.jn.langx.proxy.aop;

import com.jn.langx.proxy.AbstractMethodInvocation;
import com.jn.langx.proxy.MethodInvocation;
import com.jn.langx.util.Emptys;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AopMethodInvocation extends AbstractMethodInvocation implements MethodInvocation {
    private List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
    private int currentInterceptorIndex = -1;

    public AopMethodInvocation(Object proxy, Object target, Method method, Object[] arguments) {
        super(proxy, target, method, arguments);
    }

    public void setInterceptors(List<MethodInterceptor> interceptors) {
        if (Emptys.isNotEmpty(interceptors)) {
            this.interceptors.addAll(interceptors);
        }
    }

    @Override
    public Method getJoinPoint() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object proceed() throws Throwable {
        if (currentInterceptorIndex == interceptors.size() - 1) {
            return doJoinPoint();
        }
        currentInterceptorIndex++;
        MethodInterceptor interceptor = interceptors.get(currentInterceptorIndex);
        return interceptor.intercept(this);
    }

    protected Object doJoinPoint() {
        try {
            method.setAccessible(true);
            return method.invoke(this.target, arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
