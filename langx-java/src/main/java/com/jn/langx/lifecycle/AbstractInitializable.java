package com.jn.langx.lifecycle;

public abstract class AbstractInitializable implements Initializable{
    private volatile boolean inited = false;

    @Override
    public void init() throws InitializationException {
        if(!inited){
            doInit();
            inited = true;
        }
    }

    protected abstract void doInit() throws InitializationException;
}
