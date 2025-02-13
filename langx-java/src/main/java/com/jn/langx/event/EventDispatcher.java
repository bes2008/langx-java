package com.jn.langx.event;

/**
 * 本地使用的事件调度接口
 *
 * 此接口旨在提供一个机制，用于在特定事件发生时通知感兴趣的监听器
 * 它主要用于在应用程序内部进行事件驱动的通信和解耦
 *
 * @since 4.6.2
 */
public interface EventDispatcher {
    /**
     * 分发事件给所有注册的监听器
     *
     * 此方法遍历所有订阅者，并将事件分发给它们
     * 它是实现事件驱动架构的核心方法，使得事件的处理可以异步进行，并且解耦了事件的产生和处理
     *
     * @param event 要分发的事件，包含事件的具体信息
     * @param subscribers 一组对事件感兴趣的监听器，它们将接收并处理事件
     */
    void dispatch(DomainEvent event, Iterable<EventListener> subscribers);
}
