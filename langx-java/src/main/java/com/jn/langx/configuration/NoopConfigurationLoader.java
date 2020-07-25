package com.jn.langx.configuration;

public class NoopConfigurationLoader<T extends Configuration> implements ConfigurationLoader<T>{
    @Override
    public T load(String id) {
        return null;
    }
}
