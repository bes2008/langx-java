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
 * @param <EVENT> 一个泛型参数，表示此发布者可以处理的事件类型，必须是DomainEvent的子类。
 */
public interface EventPublisher<EVENT extends DomainEvent> extends EventBus<EVENT> {
    /**
     * 发布一个事件。
     *
     * @param event 要发布的事件对象，必须是EVENT类型。
     */
    void publish(EVENT event);

    /**
     * 为特定事件域添加一个事件监听器。
     *
     * @param eventDomain 事件域的字符串标识符。
     * @param listener 要添加的事件监听器。
     */
    void addEventListener(String eventDomain, EventListener listener);

    /**
     * 在事件监听器列表的开头处添加一个事件监听器。
     *
     * @param eventDomain 事件域的字符串标识符。
     * @param listener 要添加到列表开头的事件监听器。
     */
    void addFirst(String eventDomain, EventListener listener);

    /**
     * 从特定事件域中移除一个事件监听器。
     *
     * @param eventDomain 事件域的字符串标识符。
     * @param listener 要移除的事件监听器。
     */
    void removeEventListener(String eventDomain, EventListener listener);

    /**
     * 获取特定事件域的所有事件监听器。
     *
     * @param eventDomain 事件域的字符串标识符。
     * @return 一个包含所有事件监听器的列表。
     */
    List<EventListener> getListeners(String eventDomain);
}
