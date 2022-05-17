package com.jn.langx.event.local;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.event.EventDispatcher;
import com.jn.langx.event.EventListener;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
/**
 * @since 4.6.2
 */
public class SimpleEventDispatcher implements EventDispatcher {
    public static final SimpleEventDispatcher INSTANCE = new SimpleEventDispatcher();

    @Override
    public void dispatch(final DomainEvent event, Iterable<EventListener> subscribers) {
        Collects.forEach(subscribers, new Consumer<EventListener>() {
            @Override
            public void accept(EventListener eventListener) {
                eventListener.on(event);
            }
        });
    }
}
