package com.jn.langx.environment;

public interface Environment {
    String getProperty(String key);

    String getProperty(String key, String valueIfAbsent);

}
