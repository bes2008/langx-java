package com.jn.langx.invocation;

import java.lang.reflect.Member;

/**
 * The Invocation interface represents an invocation of a method or constructor.
 * It is primarily used in the aspect-oriented programming to provide information about the join point.
 *
 * @param <M> the type of the join point, such as Method or Constructor.
 */
public interface Invocation<M extends Member> {

    /**
     * Returns the arguments of the method or constructor invocation.
     *
     * @return an array of Object containing the arguments of the invocation.
     */
    Object[] getArguments();

    /**
     * Returns the Method object for a method invocation,
     * or the Constructor object for a constructor invocation.
     *
     * @return the join point object, Method or Constructor.
     */
    M getJoinPoint();

    /**
     * Returns the target object on which the method is invoked.
     * If it is a static method invocation, the class object is returned.
     *
     * @return the target object of the invocation.
     */
    Object getThis();

    /**
     * Proceeds with the original method or constructor invocation.
     * This method allows the invocation to continue its execution, possibly after some preprocessing.
     *
     * @return the result of the method or constructor invocation.
     * @throws Throwable if an error occurs during the invocation.
     */
    Object proceed() throws Throwable;
}
