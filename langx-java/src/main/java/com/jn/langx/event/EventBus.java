package com.jn.langx.event;

import com.jn.langx.Named;

/**
 * @since 4.6.2
 */
public interface EventBus<EVENT extends DomainEvent> extends Named {
    /**
     * 发布一个 event 消息
     * 将事件发布到事件总线上，以便事件的订阅者可以接收到该事件并进行相应的处理
     *
     * @param event 要发布的事件，类型为 EVENT，这是一个继承了 DomainEvent 的类
     */
    void publish(EVENT event);

    /**
     * 获取 Event Bus 的名称
     * 名称用于标识不同的事件总线，有助于在复杂的系统中组织和区分不同的事件流
     *
     * @return 返回 bus route 名称，这是一个字符串值，用于标识特定的事件总线
     */
    @Override
    String getName();
}
