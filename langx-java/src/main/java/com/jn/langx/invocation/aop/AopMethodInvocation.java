package com.jn.langx.invocation.aop;

import com.jn.langx.invocation.GenericMethodInvocation;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AopMethodInvocation extends GenericMethodInvocation {
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
    public Object proceed() throws Throwable {
        if (currentInterceptorIndex == interceptors.size() - 1) {
            return doProceed();
        }
        currentInterceptorIndex++;
        MethodInterceptor interceptor = interceptors.get(currentInterceptorIndex);
        return interceptor.intercept(this);
    }

    protected Object doProceed() {
        try {
            Method method = getJoinPoint();
            Reflects.makeAccessible(method);
            return method.invoke(this.getThis(), getArguments());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
