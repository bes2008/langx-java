package com.jn.langx.event;

import com.jn.langx.util.function.Function;

import java.util.List;
/**
 * 选择事件总线的接口，用于从候选事件总线列表中选择出合适的事件总线
 * 该接口扩展了Function接口，接收一个事件总线列表并返回一个经过选择的事件总线列表
 *
 * @since 4.6.2
 */
public interface EventBusSelector extends Function<List<EventBus>, List<EventBus>> {
    /**
     * 对候选事件总线列表进行选择，返回一个经过筛选的事件总线列表
     *
     * @param candidateBuses 候选的事件总线列表
     * @return 经过选择的事件总线列表
     */
    List<EventBus> apply(List<EventBus> candidateBuses);

    /**
     * 选择所有事件总线的策略，这是一个默认实现，直接返回所有候选事件总线
     * 该策略不进行任何筛选，适用于需要处理所有事件总线的场景
     */
    EventBusSelector SELECT_ALL = new EventBusSelector() {
        @Override
        public List<EventBus> apply(List<EventBus> candidateBuses) {
            return candidateBuses;
        }
    };
}
