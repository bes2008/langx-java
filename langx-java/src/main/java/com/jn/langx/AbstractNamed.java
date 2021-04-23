package com.jn.langx;

public abstract class AbstractNamed implements Named {
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
