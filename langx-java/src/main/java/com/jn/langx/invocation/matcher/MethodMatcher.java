package com.jn.langx.invocation.matcher;

import com.jn.langx.invocation.MethodInvocation;

import java.lang.reflect.Method;

public interface MethodMatcher {
    /**
     * 用于静态匹配，也就是未必是在调用方法时进行匹配
     */
    boolean matches(Method method);

    /**
     * 用于调用方法时进行匹配
     */
    boolean matches(MethodInvocation invocation);

    /**
     * @return 是否只能用于匹配 MethodInvocation
     */
    boolean isInvocationMatcher();
}
