package com.jn.langx.invocation.proxy.invoker;

import com.jn.langx.invocation.proxy.SimpleInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokerInvocationHandler extends SimpleInvocationHandler implements InvocationHandler {
    private Invoker invoker;

    public InvokerInvocationHandler(Object target, Invoker invoker) {
        super(target);
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (invoker != null) {
            return invoker.invoke(proxy, method, args);
        } else {
            return super.invoke(proxy, method, args);
        }
    }
}
