package com.jn.langx.util.io.close;


import com.jn.langx.util.collection.Collects;

import java.io.Closeable;
import java.util.List;

public class CloseableCloser extends AbstractCloser<Closeable> {
    @Override
    protected void doClose(Closeable closeable) throws Exception{
        closeable.close();
    }

    @Override
    public List<Class> applyTo() {
        return Collects.<Class>newArrayList(Closeable.class);
    }
}

