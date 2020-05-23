package com.jn.langx.classpath.cp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Preconditions;

import java.util.Set;

public class ClassLoaderClasspath extends AbstractClasspath {
    private ClassLoader classLoader;

    public ClassLoaderClasspath(@NonNull ClassLoader loader) {
        Preconditions.checkNotNull(loader);
        this.classLoader = loader;
    }

    @Override
    public Resource findResource(String relativePath, boolean isClass) {
        relativePath = Classpaths.getPath(relativePath, isClass);
        return new ClassPathResource(relativePath, classLoader);
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
