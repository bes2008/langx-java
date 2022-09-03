package com.jn.langx.invocation.matcher;

import java.lang.reflect.Method;

public interface MethodMatcher extends MethodInvocationMatcher{
    /**
     * 用于静态匹配，也就是未必是在调用方法时进行匹配
     */
    Boolean matches(Method method);

    /**
     * @return 是否只能用于匹配 MethodInvocation
     */
    boolean isInvocationMatcher();
}
