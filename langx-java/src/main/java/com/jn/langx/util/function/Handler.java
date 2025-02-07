package com.jn.langx.util.function;

/**
 * Handler接口定义了一个处理消息的通用方法
 * 它允许实现者处理特定类型的消息
 *
 * @param <I> 指定消息的类型，使得处理方法可以针对特定类型的消息进行处理
 */
public interface Handler<I> {
    /**
     * 处理指定类型的消息
     *
     * @param message 要处理的消息，其类型为接口定义的泛型类型
     */
    void handle(I message);
}
