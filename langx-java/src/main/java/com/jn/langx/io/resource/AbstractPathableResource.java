package com.jn.langx.io.resource;

public abstract class AbstractPathableResource<T> extends AbstractResource<T> implements Pathable, Urlable {
    private String path;

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
