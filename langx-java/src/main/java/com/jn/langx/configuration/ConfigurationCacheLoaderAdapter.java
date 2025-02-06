package com.jn.langx.configuration;

import com.jn.langx.cache.AbstractCacheLoader;

public class ConfigurationCacheLoaderAdapter<T extends Configuration> extends AbstractCacheLoader<String, T> {
    private ConfigurationLoader<T> configurationLoader;

    public ConfigurationCacheLoaderAdapter(ConfigurationLoader<T> configurationLoader) {
        this.configurationLoader = configurationLoader;
    }

    @Override
    public T load(String key) {
        return configurationLoader.load(key);
    }
}
