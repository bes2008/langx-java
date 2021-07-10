package com.jn.langx;

public abstract class AbstractNameable implements Nameable {
    protected String name;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
