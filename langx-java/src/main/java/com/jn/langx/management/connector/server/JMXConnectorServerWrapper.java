package com.jn.langx.management.connector.server;

import com.jn.langx.lifecycle.Lifecycle;

/**
 * JMXConnectorServerWrapper接口扩展了Lifecycle接口，旨在提供对JMX连接服务器的包装。
 * 它的目标是为JMX连接服务器提供一个生命周期管理的接口，使得服务器的启动、停止和管理更为便捷。
 * 通过实现这个接口，开发人员可以创建可管理的JMX连接服务器实例，从而集成到各种管理系统中。
 *
 * @see javax.management.remote.JMXConnectorServer
 * @see Lifecycle
 */
public interface JMXConnectorServerWrapper extends Lifecycle {
    // 此接口没有定义额外的方法，因为它主要依赖于Lifecycle接口来管理JMX连接服务器的生命周期。
}
