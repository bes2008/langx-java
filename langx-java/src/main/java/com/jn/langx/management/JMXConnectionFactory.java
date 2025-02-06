package com.jn.langx.management;

/**
 * JMX连接工厂接口
 * 用于创建和管理JMX连接
 */
public interface JMXConnectionFactory {
    /**
     * 获取JMX连接
     *
     * @param configuration 连接器配置信息，包含建立连接所需的各种参数
     * @return JMXConnection JMX连接对象，通过该对象可以对远程或本地的MBean进行操作
     *
     * 此方法根据提供的配置信息建立JMX连接，允许用户通过该连接对Java应用进行监控和管理
     * 它抽象了连接的创建过程，使得用户不需要关心连接的具体实现细节
     */
    JMXConnection getConnection(final ConnectorConfiguration configuration);
}
