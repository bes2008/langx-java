package com.jn.langx.io.resource;

public interface ResourceLoader {
    Resource loadResource(String path);
    ClassLoader getClassLoader();
}
