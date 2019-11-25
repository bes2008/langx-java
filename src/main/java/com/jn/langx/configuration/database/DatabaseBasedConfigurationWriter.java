package com.jn.langx.configuration.database;

import com.jn.langx.configuration.Configuration;
import com.jn.langx.configuration.ConfigurationWriter;

public abstract class DatabaseBasedConfigurationWriter<T extends Configuration> implements ConfigurationWriter<T> {
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
