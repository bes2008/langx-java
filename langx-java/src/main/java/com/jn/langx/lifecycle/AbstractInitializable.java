package com.jn.langx.lifecycle;

public abstract class AbstractInitializable implements Initializable {
    protected volatile boolean inited = false;

    @Override
    public void init() throws InitializationException {
        if (!inited) {
            inited = true;
            doInit();
        }
    }

    protected void doInit() throws InitializationException {
    }

}
