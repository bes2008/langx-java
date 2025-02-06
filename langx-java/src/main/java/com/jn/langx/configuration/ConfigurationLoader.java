package com.jn.langx.configuration;

import java.util.Map;

/**
 * 配置加载器接口，用于加载配置信息
 * 该接口是一个泛型接口，允许加载的配置类型在实现时指定
 *
 * @param <T> 配置类型，必须是Configuration的子类
 */
public interface ConfigurationLoader<T extends Configuration> {

    /**
     * 加载特定id的配置信息
     *
     * @param id 配置的唯一标识符
     * @return 返回加载的配置对象，如果找不到则返回null
     */
    T load(String id);

    /**
     * 加载所有的配置信息
     *
     * @return 返回一个包含所有配置的映射，键为配置的id，值为配置对象
     */
    Map<String, T> loadAll();
}
