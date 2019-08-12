package com.jn.langx;

public interface Factory<I,O> {
    O create(I input);
}
