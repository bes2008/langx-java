package com.jn.langx.util.struct;

public class NetworkAddress {
    private String host;
    private int port;

    public NetworkAddress(){

    }

    public NetworkAddress(String host, int port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "NetworkAddress{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
