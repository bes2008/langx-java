package com.jn.langx.lifecycle;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.io.close.AbstractCloser;

import java.util.List;

public class LifecycleCloser extends AbstractCloser<Lifecycle> {
    @Override
    public List<Class> applyTo() {
        return Collects.<Class>newArrayList(Lifecycle.class);
    }

    @Override
    protected void doClose(Lifecycle lifecycle) throws Throwable {
        lifecycle.shutdown();
    }
}
