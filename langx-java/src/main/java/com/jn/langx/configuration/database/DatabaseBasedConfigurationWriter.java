package com.jn.langx.configuration.database;

import com.jn.langx.configuration.Configuration;
import com.jn.langx.configuration.ConfigurationWriter;

public abstract class DatabaseBasedConfigurationWriter<T extends Configuration> implements ConfigurationWriter<T> {
    @Override
    public boolean isSupportsWrite() {
        return false;
    }

    @Override
    public boolean isSupportsRewrite() {
        return false;
    }

    @Override
    public boolean isSupportsRemove() {
        return false;
    }

}
