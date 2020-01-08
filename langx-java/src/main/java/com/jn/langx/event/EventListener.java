package com.jn.langx.event;

/**
 * @param <EVENT>
 * @author jinuo.fang
 */
public interface EventListener<EVENT extends DomainEvent> extends java.util.EventListener {
    void on(EVENT event);
}
