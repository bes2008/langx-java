package com.jn.langx.configuration;

public class NoopConfigurationWriter<T extends Configuration> implements ConfigurationWriter<T>{
    @Override
    public boolean isSupportsWrite() {
        return false;
    }

    @Override
    public void write(T configuration) {

    }

    @Override
    public boolean isSupportsRewrite() {
        return false;
    }

    @Override
    public void rewrite(T configuration) {

    }

    @Override
    public boolean isSupportsRemove() {
        return false;
    }

    @Override
    public void remove(String id) {

    }
}
