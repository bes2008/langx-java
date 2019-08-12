package com.jn.langx;

public interface Delegatable<T> {
    T getDelegate();
    void setDelegate(final T delegate);
}
