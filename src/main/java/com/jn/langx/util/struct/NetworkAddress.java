/*
 *  Copyright (c) 2016
 *  BES Software Corporation.
 *  All Rights Reserved
 *      Revision History:
 *                                 Modification       Tracking
 *  Author (Email ID)              Date               Number               Description
 *  -------------------------------------------------------------------------------------------
 *  jinuo.fang                     2019-04-16                              Initial version
 *
 */

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
