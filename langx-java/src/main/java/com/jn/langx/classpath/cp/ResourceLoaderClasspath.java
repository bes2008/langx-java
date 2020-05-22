package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.ResourceLoader;

public class ResourceLoaderClasspath extends AbstractClasspath {
    private ResourceLoader loader;

    public ResourceLoaderClasspath(ResourceLoader loader) {
        this.loader = loader;
    }

    @Override
    public Resource findResource(String path, boolean isClass) {
        path = Classpaths.getPath(path, isClass);
        return loader.loadResource(path);
    }
}
