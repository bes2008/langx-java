package com.jn.langx.pipeline;

import com.jn.langx.annotation.Singleton;

/**
 * 定义一个处理程序的单例接口，用于处理入站和出站的数据
 */
@Singleton
public interface Handler {
    /**
     * 处理入站数据的方法
     * 当有数据进入时，该方法会被调用，允许对接收到的数据进行处理
     *
     * @param ctx 上下文对象，包含处理数据所需的信息和功能
     * @throws Throwable 如果在处理入站数据时发生任何错误，可以抛出异常
     */
    void inbound(HandlerContext ctx) throws Throwable;

    /**
     * 处理出站数据的方法
     * 当有数据送出时，该方法会被调用，允许对即将发送的数据进行处理
     *
     * @param ctx 上下文对象，包含处理数据所需的信息和功能
     * @throws Throwable 如果在处理出站数据时发生任何错误，可以抛出异常
     */
    void outbound(HandlerContext ctx) throws Throwable;
}
