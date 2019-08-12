package com.jn.langx.factory;

public interface Factory<I,O> {
    O create(I input);
}
