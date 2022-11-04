package com.jn.langx.util.net;

import com.jn.langx.util.Objs;

public class NetworkAddress implements Comparable<NetworkAddress> {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        NetworkAddress that = (NetworkAddress) o;
        if (port != that.port) {
            return false;
        }
        return Objs.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return port > 0 ? (host + ":" + port) : host;
    }
}
