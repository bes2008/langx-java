package com.jn.langx.io.resource;

public abstract class AbstractPathableResource<T> extends AbstractResource<T> implements Pathable {
    private String path;

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
