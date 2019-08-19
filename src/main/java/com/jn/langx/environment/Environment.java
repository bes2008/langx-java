package com.jn.langx.environment;

public interface Environment {
    String getProperty(String key);

    void setProperty(String key, String value);
}
