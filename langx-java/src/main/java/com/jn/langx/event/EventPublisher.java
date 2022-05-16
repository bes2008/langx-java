package com.jn.langx.event;

import java.util.List;

/**
 * <pre>
 * 1、event publisher 本身不需要进行设计，因为只有调用的地方才能被称为 publisher；
 *
 * 2、这里的 event publisher 只是为了 应对在 一个Java进程内的 将event 调度给 listener的过程；
 * 也只用于在一个虚拟机内部；不作为IPC间使用；
 *
 * 3、 addListener 只有在 EventPublisher接口才有的，EventBus 上不能有，也不该有；
 * </pre>
 *
 * @param <EVENT>
 */
public interface EventPublisher<EVENT extends DomainEvent> extends EventBus<EVENT> {
    void publish(EVENT event);

    /**
     * @param eventDomain
     * @param listener
     */
    void addEventListener(String eventDomain, EventListener listener);

    void addFirst(String eventDomain, EventListener listener);

    void removeEventListener(String eventDomain, EventListener listener);

    List<EventListener> getListeners(String eventDomain);
}
