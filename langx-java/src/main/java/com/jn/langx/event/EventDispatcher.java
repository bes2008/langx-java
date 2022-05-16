package com.jn.langx.event;

/**
 * 只能本地使用
 */
public interface EventDispatcher {
    void dispatch(DomainEvent event, Iterable<EventListener> subscribers);
}
