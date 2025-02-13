package com.jn.langx.util.concurrent;

/**
 * 定义一个可执行操作的接口，支持传递可变参数执行某操作并返回执行结果
 * 此接口旨在为各种执行操作提供一个通用的入口，通过参数传递不同的执行条件或数据
 *
 * @param <V> 执行操作返回的结果类型
 */
public interface Executable<V> {
    /**
     * 执行方法，接受可变参数，允许调用者根据需要传递零个或多个参数
     * 参数的具体解释和使用方式由实现此接口的具体类定义
     *
     * @param parameters 可变参数数组，包含执行操作所需的所有参数
     * @return V 执行操作的结果，类型由调用者指定
     * @throws Exception 如果执行过程中发生错误，则抛出异常
     */
    V execute(Object... parameters) throws Exception;
}
