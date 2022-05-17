package com.jn.langx.event;

/**
 * @since 4.6.2
 */
public interface IpcEventBus extends EventBus<DomainEvent> {
    @Override
    void publish(DomainEvent event);
}
