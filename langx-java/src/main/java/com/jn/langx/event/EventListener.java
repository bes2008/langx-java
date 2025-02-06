package com.jn.langx.event;

/**
 * 定义一个事件监听器接口，用于处理特定类型的领域事件。
 * 通过泛型 EVENT，允许实现类指定感兴趣的领域事件类型。
 *
 * @param <EVENT> 泛型参数，表示此事件监听器感兴趣的领域事件类型。
 * @author jinuo.fang
 */
public interface EventListener<EVENT extends DomainEvent> extends java.util.EventListener {
    /**
     * 处理发生特定领域事件时的动作。
     * 当一个领域事件发生时，该方法会被调用，允许实现者执行相应的业务逻辑。
     *
     * @param event 发生的领域事件实例，提供对事件相关数据的访问。
     */
    void on(EVENT event);
}
