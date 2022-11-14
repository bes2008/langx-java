package com.jn.langx.util.concurrent;

public interface Executable<V> {
    V execute(Object... parameters) throws Exception;
}