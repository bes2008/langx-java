package com.jn.langx.management;

import com.jn.langx.configuration.ConfigurationLoader;

/**
 * ConnectorConfigurationLoader 接口继承自 ConfigurationLoader 接口，专门用于加载连接器的配置信息
 * 它的主要作用是提供一个标准的方法来加载特定连接器的配置，确保配置的正确性和一致性
 */
public interface ConnectorConfigurationLoader extends ConfigurationLoader<ConnectorConfiguration> {
    /**
     * 加载指定连接器的配置信息
     *
     * @param id 连接器的唯一标识符，用于指定需要加载配置的连接器
     * @return 返回 ConnectorConfiguration 对象，包含了连接器的配置信息
     *
     * 此方法允许通过提供连接器的唯一标识符来加载其配置信息，便于在大型系统中管理多个连接器的配置
     */
    @Override
    ConnectorConfiguration load(String id);
}
