package com.jn.langx.event;

public interface EventPublisherAware {
    EventPublisher getEventPublisher();

    void setEventPublisher(EventPublisher publisher);
}
