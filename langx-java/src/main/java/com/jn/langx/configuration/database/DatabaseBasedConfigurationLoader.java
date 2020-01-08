package com.jn.langx.configuration.database;


import com.jn.langx.configuration.Configuration;
import com.jn.langx.configuration.ConfigurationLoader;

import java.util.List;

public abstract class DatabaseBasedConfigurationLoader<T extends Configuration> implements ConfigurationLoader<T> {
    public abstract List<T> loadAll();
}
