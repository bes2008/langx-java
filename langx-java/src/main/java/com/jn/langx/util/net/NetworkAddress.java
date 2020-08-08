package com.jn.langx.util.net;

public class NetworkAddress implements Comparable<NetworkAddress>{
    private String host;
    private int port;

    public NetworkAddress() {

    }

    public NetworkAddress(String host, int port) {
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
    public int compareTo(NetworkAddress o) {
        return this.toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return port> 0 ?(host+":"+port) : host;
    }
}
