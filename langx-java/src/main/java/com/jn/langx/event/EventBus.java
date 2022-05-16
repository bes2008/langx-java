package com.jn.langx.event;

public interface EventBus<EVENT extends DomainEvent> {
    /**
     * 发布一个 event 消息
     */
    void publish(EVENT event);
}
