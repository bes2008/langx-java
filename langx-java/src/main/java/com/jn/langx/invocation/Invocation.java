package com.jn.langx.invocation;

import java.lang.reflect.Member;

public interface Invocation<M extends Member> {

    Object[] getArguments();

    /**
     * MethodInvocation will return the Method object,
     * ConstructorInvocation will return the Constructor object
     *
     * @return the join point
     */
    M getJoinPoint();

    Object getThis();

    Object proceed() throws Throwable;
}
