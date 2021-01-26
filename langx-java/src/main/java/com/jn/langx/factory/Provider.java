package com.jn.langx.factory;

public interface Provider<I, O> extends Factory<I, O> {
    @Override
    O get(I input);
}
