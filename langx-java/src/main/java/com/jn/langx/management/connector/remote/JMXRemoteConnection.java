package com.jn.langx.management.connector.remote;

import com.jn.langx.management.JMXConnection;
import com.jn.langx.util.logging.Loggers;

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
                Loggers.getLogger(JMXRemoteConnection.class).warn(e.getMessage(),e);
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
