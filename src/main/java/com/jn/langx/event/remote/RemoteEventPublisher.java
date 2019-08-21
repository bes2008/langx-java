package com.jn.langx.event.remote;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.event.EventPublisher;

public interface RemoteEventPublisher<EVENT extends DomainEvent> extends EventPublisher<EVENT> {
}
