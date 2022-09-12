package com.jn.langx.java8.util.io;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.io.close.AbstractCloser;

import java.util.List;

public class AutoCloseableCloser extends AbstractCloser<AutoCloseable> {
    @Override
    public List<Class> applyTo() {
        return Collects.<Class>newArrayList(AutoCloseable.class);
    }

    @Override
    protected void doClose(AutoCloseable autoCloseable) throws Throwable {
        autoCloseable.close();
    }
}
