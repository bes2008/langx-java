package com.jn.langx.util.progress;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.event.EventPublisher;
import com.jn.langx.event.EventPublisherAware;
import com.jn.langx.event.local.SimpleEventPublisher;
import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.registry.GenericRegistry;

/**
 * @since 4.4.2
 */
public class ProgressTracer extends AbstractInitializable implements EventPublisherAware {
    @NonNull
    private EventPublisher eventPublisher;
    @NonNull
    private GenericRegistry<ProgressSource> tracing;


    @Override
    protected void doInit() throws InitializationException {
        if(eventPublisher==null){
            setEventPublisher(new SimpleEventPublisher());
        }
        if(tracing==null){
            setTracing(new GenericRegistry<ProgressSource>());
        }
    }

    public void begin(ProgressSource source) {
        if (!tracing.names().contains(source.getName())) {
            tracing.register(source);
        }
        ProgressEvent event = new ProgressEvent(ProgressEventType.START, source);
        eventPublisher.publish(event);

    }

    public void finish(ProgressSource source) {
        this.tracing.unregister(source.getName());
        ProgressEvent event = new ProgressEvent(ProgressEventType.FINISH, source);
        eventPublisher.publish(event);
    }

    public void updateProcess(ProgressSource source) {
        if (this.tracing.contains(source.getName())) {
            ProgressEvent event = new ProgressEvent(ProgressEventType.UPDATE, source);
            eventPublisher.publish(event);
        }
    }


    @Override
    public EventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        this.eventPublisher = eventPublisher;
    }

    public GenericRegistry<ProgressSource> getTracing() {
        return tracing;
    }

    public void setTracing(GenericRegistry<ProgressSource> sourceRegistry) {
        this.tracing = sourceRegistry;
    }

}
