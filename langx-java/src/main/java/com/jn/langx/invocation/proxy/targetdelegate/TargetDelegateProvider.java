package com.jn.langx.invocation.proxy.targetdelegate;

import com.jn.langx.Provider;
import com.jn.langx.invocation.MethodInvocation;

/**
 * TargetDelegateProvider接口扩展了Provider接口，用于在运行时提供目标方法调用的委托对象
 * 它定义了一个标准，使得不同的实现可以根据方法调用来动态地提供或创建一个代理对象
 * 这种机制常用于AOP（面向切面编程）、动态代理或服务查找等场景
 */
public interface TargetDelegateProvider extends Provider<MethodInvocation, Object> {
}
