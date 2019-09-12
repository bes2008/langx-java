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

package com.jn.langx.management.connector.local.platform;

import com.jn.easyjmx.connector.Configuration;
import com.jn.easyjmx.connector.JMXConnection;
import com.jn.easyjmx.connector.JMXConnectionFactory;
import com.jn.easyjmx.connector.local.JMXLocalConnection;

import java.lang.management.ManagementFactory;

public class PlatformLocalConnectionFactory implements JMXConnectionFactory {
    @Override
    public JMXConnection getConnection(final Configuration config) {
        return new JMXLocalConnection(ManagementFactory.getPlatformMBeanServer());
    }
}
