package com.jn.langx.event.local;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.event.EventListener;
import com.jn.langx.event.EventPublisher;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleEventPublisher implements EventPublisher {
    private WrappedNonAbsentMap<String, List<EventListener>> listenerMap = Collects.wrapAsNonAbsentMap(new ConcurrentHashMap<String, List<EventListener>>(), new Supplier<String, List<EventListener>>() {
        @Override
        public List<EventListener> get(String input) {
            return new ArrayList<EventListener>();
        }
    });

    @Override
    public void publish(final DomainEvent event) {
        Collects.forEach(listenerMap.getIfPresent(event.getDomain()), new Consumer<EventListener>() {
            @Override
            public void accept(EventListener listener) {
                listener.on(event);
            }
        });
    }

    @Override
    public void addEventListener(String eventDomain, EventListener listener) {
        listenerMap.get(eventDomain).add(listener);
    }

    @Override
    public void addFirst(String eventDomain, EventListener listener) {
        listenerMap.get(eventDomain).add(0, listener);
    }
}
