package com.jn.langx.util.spi;

import com.jn.langx.Provider;

import java.util.Iterator;

public interface ServiceProvider<T> extends Provider<Class<T>, Iterator<T>> {
    @Override
    Iterator<T> get(Class<T> serviceClass);
}
