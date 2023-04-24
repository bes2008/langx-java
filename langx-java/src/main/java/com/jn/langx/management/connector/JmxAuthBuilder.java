package com.jn.langx.management.connector;

import java.util.HashMap;
import java.util.Map;

public class JmxAuthBuilder {
    public static Map<String, ?> buildAuth(final String user, final String password) {
        final Map auth = new HashMap();
        auth.put("jmx.remote.credentials", new String[]{user, password});
        return auth;
    }
    private JmxAuthBuilder(){

    }
}
