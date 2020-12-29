package com.jn.langx.invocation.aop;

import com.jn.langx.invocation.MethodInvocation;
import com.jn.langx.invocation.matcher.MethodMatcher;

public abstract class SparseMethodInterceptor implements MethodInterceptor, MethodMatcher {
    @Override
    public Object intercept(MethodInvocation invocation) throws Throwable {
        boolean matched = isInvocationMatcher() ? matches(invocation) : matches(invocation.getJoinPoint());
        if (matched) {
            return doIntercept(invocation);
        } else {
            return invocation.proceed();
        }
    }

    protected abstract Object doIntercept(MethodInvocation invocation);

    @Override
    public boolean isInvocationMatcher() {
        return false;
    }
}
