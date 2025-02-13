package com.jn.langx.configuration;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.text.StringTemplates;

public class ConfigurationEvent<T extends Configuration> extends DomainEvent<T> {
    private ConfigurationEventType eventType;

    public ConfigurationEvent(String eventDomain, T t) {
        super(eventDomain, t);
    }

    public ConfigurationEventType getEventType() {
        return eventType;
    }

    public void setEventType(ConfigurationEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return StringTemplates.formatWithIndex("{domain: {0}, eventType:{1}, source: {2}}", getDomain(), eventType.name(), getSource().toString());
    }
}
