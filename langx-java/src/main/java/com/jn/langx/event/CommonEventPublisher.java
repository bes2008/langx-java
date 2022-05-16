package com.jn.langx.event;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.function.Supplier;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CommonEventPublisher implements EventPublisher {
    private EventDispatcher dispatcher;

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public void publish(final DomainEvent event) {
        List<EventListener> listeners = getListeners(event.getDomain());
        getDispatcher().dispatch(event, listeners);
    }

    public void setDispatcher(EventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    private WrappedNonAbsentMap<String, Set<EventListener>> listenerMap = Collects.wrapAsNonAbsentMap(new ConcurrentHashMap<String, Set<EventListener>>(), new Supplier<String, Set<EventListener>>() {
        @Override
        public Set<EventListener> get(String input) {
            return new LinkedHashSet<EventListener>();
        }
    });

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
