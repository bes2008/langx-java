package com.jn.langx.util.reflect;

import com.jn.langx.proxy.SimpleInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Proxys {

    public static Class<?> getProxyClass(Class<?>... interfaces) {
        return Proxy.getProxyClass(Thread.currentThread().getContextClassLoader(), interfaces);
    }

    public static Class<?> getProxyClass(ClassLoader loader, Class<?>... interfaces) {
        return Proxy.getProxyClass(loader, interfaces);
    }

    public static boolean isProxyClass(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        return Proxy.isProxyClass(clazz);
    }

    public static Object newProxyInstance(InvocationHandler invocationHandler, Class<?>[] interfaces) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, invocationHandler);
    }

    public static Object newProxyInstance(ClassLoader loader, InvocationHandler invocationHandler, Class<?>[] interfaces) {
        return Proxy.newProxyInstance(loader, interfaces, invocationHandler);
    }

    public static InvocationHandler getInvocationHandler(Object proxyInstance) {
        return Proxy.getInvocationHandler(proxyInstance);
    }

    public static Object newSimpleProxy(Object object) {
        InvocationHandler invocationHandler = new SimpleInvocationHandler(object);
        return newProxyInstance(object.getClass().getClassLoader(), invocationHandler, object.getClass().getInterfaces());
    }

    public static Object newSimpleProxy(Class clazz) {
        try {
            Object target = clazz.newInstance();
            InvocationHandler invocationHandler = new SimpleInvocationHandler(target);
            return newProxyInstance(clazz.getClassLoader(), invocationHandler, clazz.getInterfaces());
        } catch (Throwable ex) {
            return new RuntimeException(ex);
        }
    }

}
