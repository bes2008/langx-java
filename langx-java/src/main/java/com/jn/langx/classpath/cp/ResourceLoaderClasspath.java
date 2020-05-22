package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.ResourceLoader;

public class ResourceLoaderClasspath implements Classpath {
    private ResourceLoader loader;

    public ResourceLoaderClasspath(ResourceLoader loader) {
        this.loader = loader;
    }

    @Override
    public Resource find(String path, boolean isClass) {
        if (isClass) {
            path = path.replace('.', '/') + ".class";
        }
        return loader.loadResource(path);
    }
}
