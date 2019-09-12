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

package com.jn.langx.management.connector.local.jndi;

import com.jn.easyjmx.connector.Configuration;
import com.jn.easyjmx.connector.JMXConnection;
import com.jn.easyjmx.connector.JMXConnectionFactory;
import com.jn.easyjmx.connector.local.JMXLocalConnection;

import javax.management.MBeanServer;
import javax.naming.InitialContext;

public class JndiLocalConnectionFactory implements JMXConnectionFactory {
    private static final String DEFAULT_CONTEXT_FACTORY = "com.sun.InitialContextFactroy";

    @Override
    public JMXConnection getConnection(final Configuration config) {
        final JndiConfiguration jndiConfig = (JndiConfiguration) config;
        final String jndi = jndiConfig.getServerJndi();
        MBeanServer server = null;
        try {
            String jndiFactroyClass = jndiConfig.getJndiFactoryClass();
            if (jndiFactroyClass == null || jndiFactroyClass.isEmpty()) {
                jndiFactroyClass = "com.sun.InitialContextFactroy";
                jndiConfig.setProperty("java.naming.factory.initial", jndiFactroyClass);
            }
            final InitialContext context = new InitialContext(jndiConfig.getProperties());
            server = (MBeanServer) context.lookup(jndi);
        } catch (Throwable t) {
        }
        if (server != null) {
            return new JMXLocalConnection(server);
        }
        return null;
    }
}
