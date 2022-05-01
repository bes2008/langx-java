package com.jn.langx.event;

import java.util.List;

public interface EventPublisher<EVENT extends DomainEvent> {
    void publish(EVENT event);

    void addEventListener(String eventDomain, EventListener listener);

    void addFirst(String eventDomain, EventListener listener);

    void removeEventListener(String eventDomain, EventListener listener);

    List<EventListener> getListeners(String eventDomain);
}
