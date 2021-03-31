package com.jn.langx.lifecycle;

public abstract class AbstractInitializable implements Initializable {
    protected volatile boolean inited = false;

    @Override
    public void init() throws InitializationException {
        if (!inited) {
            doInit();
            inited = true;
        }
    }

    protected void doInit() throws InitializationException {
    }

}
