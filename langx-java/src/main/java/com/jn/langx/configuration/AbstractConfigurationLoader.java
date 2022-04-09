package com.jn.langx.configuration;

import java.util.Map;

public class AbstractConfigurationLoader<T extends Configuration> implements ConfigurationLoader<T> {
    @Override
    public T load(String id) {
        return null;
    }

    @Override
    public Map<String, T> loadAll() {
        return null;
    }
}
