package com.jn.langx.event;

/**
 * EventPublisherAware接口用于提供对事件发布器的访问和设置功能
 * 实现该接口的类能够获取当前的事件发布器，并且可以设置或更新它
 */
public interface EventPublisherAware {
    /**
     * 获取当前的事件发布器
     *
     * @return EventPublisher 返回当前的事件发布器实例
     */
    EventPublisher getEventPublisher();

    /**
     * 设置当前的事件发布器
     *
     * @param publisher EventPublisher 类型的参数，代表要设置的事件发布器实例
     *                  通过此方法，外部可以为实现该接口的类指定一个事件发布器实例
     */
    void setEventPublisher(EventPublisher publisher);
}

