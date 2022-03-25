package com.jn.langx.util.progress;

import com.jn.langx.event.DomainEvent;

public class ProgressEvent extends DomainEvent<ProgressSource> {
    private ProgressEventType eventType;

    public ProgressEvent(ProgressEventType eventType, ProgressSource progressSource) {
        super(progressSource.getEventDomain(), progressSource);
        setEventType(eventType);
    }

    public ProgressEventType getEventType() {
        return eventType;
    }

    public void setEventType(ProgressEventType eventType) {
        this.eventType = eventType;
    }

}
