package com.jn.langx.invocation.proxy.sparse;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.invocation.GenericMethodInvocation;
import com.jn.langx.invocation.aop.MethodInterceptor;
import com.jn.langx.util.Preconditions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SparseMethodInvocationHandler<T> implements InvocationHandler {

    @NonNull
    private T target;

    @Nullable
    private MethodInterceptor interceptor;

    public SparseMethodInvocationHandler(T target) {
        Preconditions.checkNotNull(target, "the target is null");
        this.target = target;
    }

    public void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.interceptor == null) {
            return method.invoke(target, args);
        }
        return this.interceptor.intercept(new GenericMethodInvocation(proxy, target, method, args));
    }
}
