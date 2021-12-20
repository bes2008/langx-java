package com.jn.langx.classpath.cp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.classpath.Classpaths;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;

import java.util.Set;

public class ClassLoaderClasspath extends AbstractClasspath {
    private ClassLoader classLoader;
    private Location root = new Location(ClassPathResource.PREFIX,"");

    public ClassLoaderClasspath(@NonNull ClassLoader loader) {
        Preconditions.checkNotNull(loader);
        this.classLoader = loader;
    }

    @Override
    public Resource findResource(String relativePath) {
        relativePath = Classpaths.getCanonicalFilePath(relativePath);
        return new ClassPathResource(relativePath, classLoader);
    }

    @Override
    public Location getRoot() {
        return root;
    }

    @Override
    public Set<Location> allResources() {
        return Collects.immutableSet();
    }
}
