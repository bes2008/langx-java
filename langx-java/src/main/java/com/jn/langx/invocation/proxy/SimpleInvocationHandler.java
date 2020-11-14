package com.jn.langx.invocation.proxy;

import com.jn.langx.invocation.GenericMethodInvocation;
import com.jn.langx.invocation.MethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * ProxyInstance -> invocation handler -> Target
 *
 * @author jinuo.fang
 */
public class SimpleInvocationHandler implements InvocationHandler {
    protected Object target;

    public SimpleInvocationHandler() {
    }

    public SimpleInvocationHandler(Object target) {
        setTarget(target);
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return execute(new GenericMethodInvocation(proxy, target, method, args));
    }

    protected Object execute(MethodInvocation methodInvocation) throws Throwable {
        return methodInvocation.getJoinPoint().invoke(methodInvocation.getThis(), methodInvocation.getArguments());
    }
}
