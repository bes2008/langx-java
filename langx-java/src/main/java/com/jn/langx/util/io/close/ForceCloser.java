package com.jn.langx.util.io.close;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.Reflects;

import java.util.List;

public class ForceCloser extends AbstractCloser<Object> {
    @Override
    public List<Class> applyTo() {
        return Collects.<Class>newArrayList(Object.class);
    }

    @Override
    protected void doClose(Object o) throws Exception {
        Reflects.invokeAnyMethodForcedIfPresent(o, "close", null, null);
    }
}
