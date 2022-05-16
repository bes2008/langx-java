package com.jn.langx;

public interface Delegatable<T> extends DelegateHolder<T> {
    @Override
    T getDelegate();

    void setDelegate(final T delegate);
}
