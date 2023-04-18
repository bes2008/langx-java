package com.jn.langx.util.io.close;

import com.jn.langx.util.collection.Collects;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;

public class ConnectionCloser extends AbstractCloser<Connection> {
    @Override
    protected void doClose(Connection connection) throws Exception {
        connection.close();
    }

    @Override
    public List<Class> applyTo() {
        return Collects.<Class>newArrayList(Connection.class);
    }
}
