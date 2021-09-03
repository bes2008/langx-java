package com.jn.langx;

public interface Provider<I, O> extends Factory<I, O> {
    @Override
    O get(I input);
}
