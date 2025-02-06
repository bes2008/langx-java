package com.jn.langx.event;
/**
 * IpcEventBus接口扩展了EventBus接口，专门用于处理跨进程通信（IPC）事件的发布
 * 它继承了EventBus接口的所有功能，并针对IPC场景进行了定制
 *
 * @since 4.6.2
 */
public interface IpcEventBus extends EventBus<DomainEvent> {
    /**
     * 发布一个领域事件到所有订阅了该事件类型的订阅者
     * 此方法主要用于跨进程通信，将事件通知给所有感兴趣的监听器
     *
     * @param event 要发布的领域事件，不能为空
     */
    @Override
    void publish(DomainEvent event);
}
