package com.jn.langx.invocation.proxy.invoker;

import java.lang.reflect.InvocationHandler;

/**
 * Invoker接口扩展了InvocationHandler，用于处理特定的调用请求。
 * 它定义了一个统一的接口，以便在运行时处理各种类型的调用。
 * 通过实现Invoker接口，可以创建一个通用的调用处理器，该处理器能够处理不同的调用请求，
 * 提供了一种灵活的方式来执行方法调用，特别是在动态代理或服务调用框架中。
 */
public interface Invoker extends InvocationHandler {
}
