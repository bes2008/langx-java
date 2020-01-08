package com.jn.langx.proxy;

import java.lang.reflect.InvocationHandler;

public class SimpleProxyFactory implements ProxyFactory<Object, Object> {
    @Override
    public Object get(Object target) {
        return newSimpleProxy(target);
    }

    public static Object newSimpleProxy(Object object) {
        InvocationHandler invocationHandler = new SimpleInvocationHandler(object);
        return Proxys.newProxyInstance(object.getClass().getClassLoader(), invocationHandler, object.getClass().getInterfaces());
    }

    public static Object newSimpleProxy(Class clazz) {
        try {
            InvocationHandler invocationHandler = new SimpleInvocationHandler();
            return Proxys.newProxyInstance(clazz.getClassLoader(), invocationHandler, clazz.getInterfaces());
        } catch (Throwable ex) {
            return new RuntimeException(ex);
        }
    }

}
