package com.jn.langx.management.connector.local.platform;

import com.jn.langx.management.Configuration;
import com.jn.langx.management.JMXConnection;
import com.jn.langx.management.JMXConnectionFactory;
import com.jn.langx.management.connector.local.JMXLocalConnection;

import java.lang.management.ManagementFactory;

public class PlatformLocalConnectionFactory implements JMXConnectionFactory {
    @Override
    public JMXConnection getConnection(final Configuration config) {
        return new JMXLocalConnection(ManagementFactory.getPlatformMBeanServer());
    }
}
