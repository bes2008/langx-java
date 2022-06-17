package com.jn.langx.query;

public interface Container {
    <T>T select(String expression);
}
