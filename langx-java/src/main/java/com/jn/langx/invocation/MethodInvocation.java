package com.jn.langx.invocation;

import java.lang.reflect.Method;

/**
 * MethodInvocation接口扩展了Invocation接口，专门用于处理方法的调用
 * 它提供了一个特定于方法调用的访问点，使得可以在方法执行前或后进行拦截和操作
 */
public interface MethodInvocation extends Invocation<Method> {
    /**
     * 获取当前方法调用的连接点
     * 这个方法返回一个Method对象，该对象表示当前正在被调用的方法
     * 通过这个对象，可以获取到方法的相关信息，如方法名、参数类型等
     *
     * @return Method对象，表示当前正在被调用的方法
     */
    @Override
    Method getJoinPoint();
}
