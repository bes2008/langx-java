package com.jn.langx.event;


public interface IpcEventBus extends EventBus<DomainEvent> {
    @Override
    void publish(DomainEvent event);
}
