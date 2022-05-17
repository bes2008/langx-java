package com.jn.langx.event;

import com.jn.langx.Named;

/**
 * @since 4.6.2
 */
public interface EventBus<EVENT extends DomainEvent> extends Named {
    /**
     * 发布一个 event 消息
     */
    void publish(EVENT event);

    /**
     * @return 返回 bus route 名（可以理解为 bus 路线名称）
     */
    @Override
    String getName();
}
