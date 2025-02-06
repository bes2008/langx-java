package com.jn.langx.invocation.aop;

import com.jn.langx.invocation.MethodInvocation;

/**
 * MethodInterceptor 接口继承自 InvocationInterceptor 接口，用于定义方法级别的拦截逻辑。
 * 它允许在目标方法执行前后、发生异常时或方法执行成功后进行拦截和处理。
 */
public interface MethodInterceptor extends InvocationInterceptor<MethodInvocation> {
    /**
     * 拦截方法调用。
     *
     * @param invocation 方法调用对象，提供了关于目标方法调用的信息，如方法、参数等。
     * @return 目标方法执行后的返回值。
     * @throws Throwable 如果目标方法调用或拦截逻辑中抛出了异常。
     */
    @Override
    Object intercept(MethodInvocation invocation) throws Throwable;
}
