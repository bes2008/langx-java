package com.jn.langx.management;

public interface JMXConnectionFactory {
    JMXConnection getConnection(final ConnectorConfiguration p0);
}
