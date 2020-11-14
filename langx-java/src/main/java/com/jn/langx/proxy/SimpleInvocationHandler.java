package com.jn.langx.proxy;

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
        return execute(new AbstractMethodInvocation(proxy, target, method, args));
    }

    protected Object execute(AbstractMethodInvocation methodInvocation) throws Throwable {
        return methodInvocation.getMethod().invoke(methodInvocation.getTarget(), methodInvocation.getArguments());
    }
}
