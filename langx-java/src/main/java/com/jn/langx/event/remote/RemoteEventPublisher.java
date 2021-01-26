package com.jn.langx.event.remote;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.event.EventListener;
import com.jn.langx.event.EventPublisher;

public interface RemoteEventPublisher<EVENT extends DomainEvent> extends EventPublisher<EVENT> {
    @Override
    void publish(EVENT event);

    @Override
    void addEventListener(String eventDomain, EventListener listener);

    @Override
    void addFirst(String eventDomain, EventListener listener);
}
