package com.jn.langx.lifecycle;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.io.close.AbstractCloser;

import java.util.List;

public class DestroyableCloser extends AbstractCloser<Destroyable> {
    @Override
    public List<Class> applyTo() {
        return Collects.<Class>newArrayList(Destroyable.class);
    }

    @Override
    protected void doClose(Destroyable destroyable) throws Exception {
        destroyable.destroy();
    }
}
