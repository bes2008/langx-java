package com.jn.langx.event;

import com.jn.langx.util.function.Function;

import java.util.List;
/**
 * @since 4.6.2
 */
public interface EventBusSelector extends Function<List<EventBus>, List<EventBus>> {
    List<EventBus> apply(List<EventBus> candidateBuses);

    EventBusSelector SELECT_ALL = new EventBusSelector() {
        @Override
        public List<EventBus> apply(List<EventBus> candidateBuses) {
            return candidateBuses;
        }
    };
}
