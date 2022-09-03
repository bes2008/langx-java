package com.jn.langx.invocation.matcher;

import com.jn.langx.invocation.MethodInvocation;

import java.lang.reflect.Method;

public class TrueMethodMatcher implements MethodMatcher {
    public static final TrueMethodMatcher INSTANCE = new TrueMethodMatcher();

    @Override
    public Boolean matches(Method method) {
        return true;
    }

    @Override
    public Boolean matches(MethodInvocation invocation) {
        return true;
    }

    @Override
    public boolean isInvocationMatcher() {
        return false;
    }
}
