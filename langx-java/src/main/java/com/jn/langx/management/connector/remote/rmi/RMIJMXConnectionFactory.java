package com.jn.langx.management.connector.remote.rmi;

import com.jn.langx.management.MBeanException;
import com.jn.langx.management.connector.remote.JMXRemoteConnectionFactory;
import com.jn.langx.management.connector.remote.RemoteConfiguration;

import javax.management.remote.JMXServiceURL;
import java.net.MalformedURLException;
import java.util.Map;

public class RMIJMXConnectionFactory extends JMXRemoteConnectionFactory {
    @Override
    protected JMXServiceURL buildJMXServiceUrl(final RemoteConfiguration rmiConfig) {
        final RMIConfiguration config = (RMIConfiguration) rmiConfig;
        String serviceName = config.getServerName();
        final boolean startRmiRegistry = config.isStartRmiRegistry();
        final String host = config.getHost();
        final int port = config.getPort();
        final StringBuilder url = new StringBuilder();
        if (serviceName == null) {
            serviceName = "jmxrmi";
        }
        if (startRmiRegistry) {
            final int rmiRegistryPort = config.getRmiRegistryPort();
            url.append("service:jmx:rmi://").append(host).append(":");
            url.append(port);
            url.append("/jndi/rmi://").append(host).append(":");
            url.append(rmiRegistryPort);
            url.append("/").append(serviceName);
        } else {
            url.append("service:jmx:rmi:///jndi/rmi//").append(host).append(":").append(port).append("/").append(serviceName);
        }
        try {
            return new JMXServiceURL(url.toString());
        } catch (MalformedURLException e) {
            throw new MBeanException(e);
        }
    }

    @Override
    protected void setEnviroment(final Map<String, Object> env, final RemoteConfiguration config) {
        env.put("jmx.remote.credentials", new String[]{config.getUser(), config.getPassword()});
    }
}
