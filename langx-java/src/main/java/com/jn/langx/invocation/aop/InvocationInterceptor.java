package com.jn.langx.invocation.aop;

import com.jn.langx.invocation.Invocation;

/**
 * InvocationInterceptor是一个泛型接口，用于在方法调用过程中拦截并处理调用
 * 它允许实现者在目标方法执行前后执行自定义逻辑，例如日志记录、性能监控或安全检查
 *
 * @param <I> 期望的调用类型，这允许拦截器针对不同类型的调用进行定制化的处理
 */
public interface InvocationInterceptor<I extends Invocation> {
    /**
     * 拦截并处理方法调用
     *
     * @param invocation 调用对象，提供了关于当前调用的信息，如方法名、参数等
     * @return Object 返回值是拦截器处理后的结果，这可能是目标方法的返回值，也可能是拦截器自定义的返回值
     * @throws Throwable 如果目标方法调用或拦截逻辑抛出异常，此方法也会抛出
     */
    Object intercept(I invocation) throws Throwable;
}
