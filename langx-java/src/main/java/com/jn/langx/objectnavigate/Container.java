package com.jn.langx.objectnavigate;

public interface Container {
    <T> T get(String expression);

    <T> void set(String expression, T value);
}
