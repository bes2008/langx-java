package com.jn.langx.util.net;

import com.jn.langx.util.Strings;
import com.jn.langx.util.Validations;
import com.jn.langx.util.struct.pair.IntegerNameValuePair;

public class HostAndPort extends IntegerNameValuePair {
    public HostAndPort(String name, int port) {
        super(Strings.trim(name), port <= 0 ? -1 : port);
    }

    public boolean isValid() {
        if (Strings.isBlank(getKey())) {
            return false;
        }
        if (getValue() <= 0) {
            return false;
        }
        return Validations.isValidPort(getValue());
    }

    @Override
    public String toString() {
        String hostname = getKey();
        String port = getValue() > 0 ? (":" + getValue()) : "";
        return hostname + port;
    }

    public static HostAndPort of(String host) throws NumberFormatException {
        if (Strings.isBlank(host)) {
            return null;
        }
        host = Strings.strip(host, "\"");
        int ipv6EndIndex = host.lastIndexOf(']');
        int hostnamePortSeparatorIndex = -1;
        if (ipv6EndIndex > 0) {
            // IPv6地址（如 [::1]:8080）
            hostnamePortSeparatorIndex = host.lastIndexOf(':', ipv6EndIndex);
        } else {
            hostnamePortSeparatorIndex = host.lastIndexOf(':');
        }
        String hostname = null;
        int port = -1;
        if (hostnamePortSeparatorIndex < 0) {
            hostname = host;
        } else {
            hostname = host.substring(0, hostnamePortSeparatorIndex);
            String portPart = host.substring(hostnamePortSeparatorIndex + 1);
            port = Integer.parseInt(portPart);
        }
        return new HostAndPort(hostname, port);
    }

}
