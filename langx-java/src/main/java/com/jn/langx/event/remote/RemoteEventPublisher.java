package com.jn.langx.event.remote;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.event.EventListener;
import com.jn.langx.event.EventPublisher;

import java.util.List;

public interface RemoteEventPublisher<EVENT extends DomainEvent> extends EventPublisher<EVENT> {
    @Override
    void publish(EVENT event);

    @Override
    void addEventListener(String eventDomain, EventListener listener);

    @Override
    void addFirst(String eventDomain, EventListener listener);

    @Override
    void removeEventListener(String eventDomain, EventListener listener);

    @Override
    List<EventListener> getListeners(String eventDomain);
}
