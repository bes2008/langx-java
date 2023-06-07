package com.jn.langx;

public interface ObjectAccessor {
    void setProperty(String property, Object value);

    Object getProperty(String property);

    Object invoke(String function, Object... args);
}
