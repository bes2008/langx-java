package com.jn.langx.configuration;

import java.util.List;

/**
 * 支持全量加载的 Loader
 * @param <T>
 */
public interface FullLoadConfigurationLoader<T extends Configuration> extends ConfigurationLoader<T> {
    List<T> loadAll();

    @Override
    T load(String id);
}
