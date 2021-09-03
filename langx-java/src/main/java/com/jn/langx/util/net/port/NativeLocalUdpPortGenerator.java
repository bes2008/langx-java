package com.jn.langx.util.net.port;

import com.jn.langx.util.io.IOs;

import java.io.IOException;
import java.net.DatagramSocket;

public class NativeLocalUdpPortGenerator implements LocalPortGenerator {
    @Override
    public Integer get() {
        DatagramSocket socket = null;
        int port = -1;
        try {
            socket = new DatagramSocket();
            port = socket.getLocalPort();
        } catch (IOException ex) {

        } finally {
            IOs.close(socket);
        }
        return port;
    }
}
