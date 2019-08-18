package com.jn.langx.proxy;

public class SimpleProxyFactory implements ProxyFactory<Object, Object> {
    @Override
    public Object get(Object target) {
        return Proxys.newSimpleProxy(target);
    }
}
