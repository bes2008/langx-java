package com.jn.langx.invocation.aop;

import com.jn.langx.Provider;
import com.jn.langx.invocation.MethodInvocation;

import java.util.List;

/**
 * 定义了一个接口，用于提供方法调用拦截链
 * 这个接口继承自Provider接口，用于生成一组方法拦截器
 * 主要用于在方法调用前后添加额外的处理逻辑
 */
public interface MethodInterceptorChainProvider extends Provider<MethodInvocation, List<MethodInterceptor>> {
    /**
     * 根据方法调用信息获取一组方法拦截器
     *
     * @param invocation 方法调用信息，包含了被调用方法的详细信息
     * @return 返回一个方法拦截器列表，用于在方法调用前后进行拦截和处理
     */
    @Override
    List<MethodInterceptor> get(MethodInvocation invocation);
}
