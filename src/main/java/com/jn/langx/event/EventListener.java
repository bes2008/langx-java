package com.jn.langx.event;

/**
 * @author jinuo.fang
 * @param <EVENT>
 */
public interface EventListener<EVENT extends DomainEvent> extends java.util.EventListener {
    void on(EVENT event);
}
