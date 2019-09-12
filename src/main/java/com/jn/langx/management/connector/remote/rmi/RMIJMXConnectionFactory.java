/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.management.connector.remote.rmi;

import com.jn.easyjmx.connector.MBeanException;
import com.jn.easyjmx.connector.remote.JMXRemoteConnectionFactory;
import com.jn.easyjmx.connector.remote.RemoteConfiguration;

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
