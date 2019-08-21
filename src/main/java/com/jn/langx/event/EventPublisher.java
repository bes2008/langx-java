package com.jn.langx.event;

public interface EventPublisher <EVENT extends DomainEvent>{
    void publish (EVENT event);
    void addEventListener(String eventDomain, EventListener listener);
    void addFirst(String eventDomain, EventListener listener);
}
