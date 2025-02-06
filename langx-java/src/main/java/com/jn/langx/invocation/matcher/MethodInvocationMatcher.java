package com.jn.langx.invocation.matcher;

import com.jn.langx.Matcher;
import com.jn.langx.invocation.MethodInvocation;

/**
 * MethodInvocationMatcher接口扩展了Matcher接口，专门用于匹配方法调用。
 * 它定义了一个方法来判断给定的方法调用是否满足某些条件。
 */
public interface MethodInvocationMatcher extends Matcher<MethodInvocation, Boolean> {

    /**
     * 判断给定的方法调用是否满足匹配条件。
     *
     * @param methodInvocation 方法调用对象，包含方法调用的相关信息。
     * @return 如果方法调用满足匹配条件，则返回true；否则返回false。
     */
    @Override
    Boolean matches(MethodInvocation methodInvocation);
}
