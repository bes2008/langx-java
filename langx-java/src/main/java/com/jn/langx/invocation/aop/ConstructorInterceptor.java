package com.jn.langx.invocation.aop;

import com.jn.langx.invocation.ConstructorInvocation;

/**
 * ConstructorInterceptor接口继承自InvocationInterceptor，专门用于拦截构造器的调用。
 * 它允许开发者在目标类的构造器被调用时执行自定义逻辑。
 */
public interface ConstructorInterceptor extends InvocationInterceptor<ConstructorInvocation> {
    /**
     * 拦截构造器调用的方法。
     *
     * @param invocation 构造器调用的上下文信息，包含了构造器调用的相关信息，如正在被调用的构造器、传入的参数等。
     * @return 返回构造器调用的结果对象。
     * @throws Throwable 如果构造器调用过程中抛出了异常，该方法也会抛出相应的异常。
     */
    @Override
    Object intercept(ConstructorInvocation invocation) throws Throwable;
}
