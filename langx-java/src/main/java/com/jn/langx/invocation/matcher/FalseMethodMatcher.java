package com.jn.langx.invocation.matcher;

import com.jn.langx.invocation.MethodInvocation;

import java.lang.reflect.Method;

public class FalseMethodMatcher implements MethodMatcher {
    public static final FalseMethodMatcher INSTANCE = new FalseMethodMatcher();

    @Override
    public boolean matches(Method method) {
        return false;
    }

    @Override
    public boolean matches(MethodInvocation invocation) {
        return false;
    }

    @Override
    public boolean isInvocationMatcher() {
        return false;
    }
}
