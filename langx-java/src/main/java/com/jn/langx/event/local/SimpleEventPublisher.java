package com.jn.langx.event.local;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.event.EventListener;
import com.jn.langx.event.EventPublisher;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Supplier;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleEventPublisher implements EventPublisher {
    private WrappedNonAbsentMap<String, Set<EventListener>> listenerMap = Collects.wrapAsNonAbsentMap(new ConcurrentHashMap<String, Set<EventListener>>(), new Supplier<String, Set<EventListener>>() {
        @Override
        public Set<EventListener> get(String input) {
            return new LinkedHashSet<EventListener>();
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
        Set<EventListener> listeners = listenerMap.get(eventDomain);
        List<EventListener> list = Collects.asList(listeners);
        list.add(listener);
        list.addAll(listeners);
        listeners = Collects.asSet(list);
        listenerMap.put(eventDomain, listeners);
    }

    @Override
    public void removeEventListener(String eventDomain, EventListener listener) {
        listenerMap.get(eventDomain).remove(listener);
    }

    @Override
    public List<EventListener> getListeners(String eventDomain) {
        return Collects.asList(listenerMap.get(eventDomain));
    }
}
