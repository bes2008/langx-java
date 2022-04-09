package com.jn.langx.configuration;

import java.util.Map;

/**
 * 支持全量加载的 Loader
 * Configurator Loader 已支持全量加载，所以不需要该接口了
 * @param <T>
 */
@Deprecated
public interface FullLoadConfigurationLoader<T extends Configuration> extends ConfigurationLoader<T> {
    Map<String,T> loadAll();

    @Override
    T load(String id);
}
