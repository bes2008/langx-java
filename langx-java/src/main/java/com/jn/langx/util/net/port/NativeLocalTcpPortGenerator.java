package com.jn.langx.util.net.port;

import com.jn.langx.util.io.IOs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NativeLocalTcpPortGenerator implements LocalPortGenerator {
    @Override
    public Integer get() {
        Socket socket = new Socket();
        int port = -1;
        try {
            socket.bind(new InetSocketAddress(0));
            port = socket.getLocalPort();
        } catch (IOException ex) {

        } finally {
            IOs.close(socket);
        }

        return port;
    }
}
