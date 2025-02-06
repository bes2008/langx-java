package com.jn.langx.configuration;

import com.jn.langx.util.Preconditions;

public class ConfigurationEventFactory<T extends Configuration> {
    private String domain;

    public ConfigurationEventFactory(String domain) {
        Preconditions.checkNotNull(domain);
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public ConfigurationEvent<T> createEvent(ConfigurationEventType type, T configuration) {
        ConfigurationEvent<T> event = new ConfigurationEvent<T>(domain, configuration);
        event.setEventType(type);
        return event;
    }
}
