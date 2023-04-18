package com.jn.langx.event;

import com.jn.langx.NameAware;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.WrappedNonAbsentMap;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.struct.counter.AtomicIntegerCounter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since 4.6.2
 */
public class CommonEventPublisher implements EventPublisher, NameAware {
    private EventDispatcher dispatcher;
    private String name;
    protected static final AtomicIntegerCounter counter = new AtomicIntegerCounter(0);
    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    public CommonEventPublisher() {
        setName("common");
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
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
