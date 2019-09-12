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

import com.jn.easyjmx.connector.JMXConnection;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import java.io.IOException;

public class JMXRemoteConnection extends JMXConnection {
    private JMXConnector connector;
    private boolean connInited;

    public JMXRemoteConnection(final MBeanServerConnection connSub) {
        super(connSub);
        this.connInited = false;
    }

    @Override
    public void close() {
        if (this.connector != null) {
            try {
                this.connector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JMXConnector getConnector() {
        return this.connector;
    }

    public void setConnector(final JMXConnector connector) {
        this.connector = connector;
    }

    public boolean isInit() {
        return this.connInited;
    }
}
