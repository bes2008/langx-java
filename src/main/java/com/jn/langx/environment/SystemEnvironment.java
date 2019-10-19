package com.jn.langx.environment;

import com.jn.langx.util.Preconditions;

public class SystemEnvironment implements Environment {
    @Override
    public String getProperty(String key) {
        Preconditions.checkNotNull(key);
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(key);
        }
        return value;
    }

    @Override
    public String getProperty(String key, String valueIfAbsent) {
        String value = getProperty(key);
        if (value != null) {
            return value;
        }
        return valueIfAbsent;
    }

    @Override
    public void setProperty(String key, String value) {
        Preconditions.checkNotNull(key);
        System.setProperty(key, value);
    }
}
