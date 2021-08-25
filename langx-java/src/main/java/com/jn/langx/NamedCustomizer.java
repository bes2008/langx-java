package com.jn.langx;

public interface NamedCustomizer<T> extends Named, Customizer<T> {
    @Override
    String getName();

    @Override
    void customize(T target);
}
