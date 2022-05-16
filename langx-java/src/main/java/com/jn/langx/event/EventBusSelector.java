package com.jn.langx.event;

import java.util.List;

public interface EventBusSelector {
    List<EventBus> select(List<EventBus> candidateBuses);

    public static final EventBusSelector SELECT_ALL = new EventBusSelector() {
        @Override
        public List<EventBus> select(List<EventBus> candidateBuses) {
            return candidateBuses;
        }
    };
}
