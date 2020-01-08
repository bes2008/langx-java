package com.jn.langx.proxy;

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
        return newProxyInstance(null, invocationHandler, interfaces);
    }

    public static Object newProxyInstance(ClassLoader loader, InvocationHandler invocationHandler, Class<?>[] interfaces) {
        if (loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
        }
        return Proxy.newProxyInstance(loader, interfaces, invocationHandler);
    }

    public static InvocationHandler getInvocationHandler(Object proxyInstance) {
        return Proxy.getInvocationHandler(proxyInstance);
    }
}
