package com.jn.langx.event;

import com.jn.langx.NameAware;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

public class EventBusStation extends GenericRegistry<EventBus> implements EventBus, NameAware {
    private String name;
    private EventBusSelector selector = EventBusSelector.SELECT_ALL;

    public void setSelector(EventBusSelector selector) {
        this.selector = selector;
    }

    @Override
    public void publish(final DomainEvent event) {
        Collects.forEach(selector.select(instances()), new Consumer<EventBus>() {
            @Override
            public void accept(EventBus eventBus) {
                eventBus.publish(event);
            }
        });
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
