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

package com.jn.langx.management.connector.remote;

import com.jn.easyjmx.connector.Configuration;
import com.jn.easyjmx.connector.JMXConnection;
import com.jn.easyjmx.connector.JMXConnectionFactory;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;
import java.util.Map;

public abstract class JMXRemoteConnectionFactory implements JMXConnectionFactory {
    @Override
    public JMXConnection getConnection(final Configuration config) {
        final RemoteConfiguration conf = (RemoteConfiguration) config;
        final JMXServiceURL serviceUrl = this.buildJMXServiceUrl(conf);
        final Map<String, Object> env = new HashMap<String, Object>();
        this.setEnviroment(env, conf);
        JMXRemoteConnection conn = null;
        try {
            final JMXConnector connector = JMXConnectorFactory.connect(serviceUrl, env);
            final MBeanServerConnection connSub = connector.getMBeanServerConnection();
            conn = new JMXRemoteConnection(connSub);
            conn.setConnector(connector);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    protected abstract JMXServiceURL buildJMXServiceUrl(final RemoteConfiguration p0);

    protected abstract void setEnviroment(final Map<String, Object> p0, final RemoteConfiguration p1);
}
