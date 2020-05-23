package com.jn.langx.classpath.cp;

import com.jn.langx.classpath.Classpaths;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.ResourceLoader;

import java.util.Set;

public class ResourceLoaderClasspath extends AbstractClasspath {
    private ResourceLoader loader;

    public ResourceLoaderClasspath(ResourceLoader loader) {
        this.loader = loader;
    }

    @Override
    public Resource findResource(String relativePath, boolean isClass) {
        relativePath = Classpaths.getPath(relativePath, isClass);
        return loader.loadResource(relativePath);
    }

    @Override
    public Location getRoot() {
        return null;
    }

    @Override
    public Set<Location> allResources() {
        return null;
    }
}
