package com.jn.langx.query;

public interface Container {
    <T> T get(String expression);

    <T> void set(String expression, T value);
}
