package com.jn.langx.invocation.proxy.sparse;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.Factory;
import com.jn.langx.invocation.aop.MethodInterceptor;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;

import java.lang.reflect.Proxy;

public class SparseProxyFactory implements Factory<Object, Object> {
    @Override
    public Object get(Object target) {
        return newProxy(target, null, null);
    }

    public static Object newProxy(@NonNull Object target, @Nullable MethodInterceptor interceptor, @Nullable Class[] interfaces) {
        Preconditions.checkNotNull(target);

        if (Emptys.isEmpty(interfaces)) {
            interfaces = target.getClass().getInterfaces();
        }

        SparseMethodInvocationHandler handler = new SparseMethodInvocationHandler(target);
        handler.setInterceptor(interceptor);
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), interfaces, handler);
    }
}
