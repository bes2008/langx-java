package com.jn.langx.invocation.matcher;

import java.lang.reflect.Method;

/**
 * MethodMatcher接口扩展了MethodInvocationMatcher，用于在更广泛的上下文中匹配方法
 * 它不仅限于在方法调用时进行匹配，还可以在静态情况下匹配方法
 */
public interface MethodMatcher extends MethodInvocationMatcher{
    /**
     * 用于静态匹配，也就是未必是在调用方法时进行匹配
     *
     * @param method 需要匹配的方法
     * @return 如果方法与匹配器的条件相符，则返回true；否则返回false
     */
    Boolean matches(Method method);

    /**
     * 判断当前匹配器是否只能用于匹配MethodInvocation
     *
     * @return 如果匹配器只能用于匹配MethodInvocation，则返回true；否则返回false
     */
    boolean isInvocationMatcher();
}
