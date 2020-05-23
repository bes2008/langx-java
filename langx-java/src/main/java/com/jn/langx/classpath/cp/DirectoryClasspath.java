package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.DirectoryBasedFileResourceLoader;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;

import java.util.Set;

public class DirectoryClasspath extends AbstractClasspath {
    private DirectoryBasedFileResourceLoader loader;

    public DirectoryClasspath(String rootDirectory) {
        loader = new DirectoryBasedFileResourceLoader(rootDirectory);
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
