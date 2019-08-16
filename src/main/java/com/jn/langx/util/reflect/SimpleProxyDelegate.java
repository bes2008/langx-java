package com.jn.langx.util.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * ProxyInstance -> Delegate -> Target
 *
 * @author jinuo.fang
 */
public class SimpleProxyDelegate implements InvocationHandler {
    private Object target;

    public SimpleProxyDelegate(Object target) {
        setTarget(target);
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target, args);
    }
}
