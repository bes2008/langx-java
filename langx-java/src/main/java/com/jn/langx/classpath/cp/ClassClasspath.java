package com.jn.langx.classpath.cp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.classpath.Classpaths;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Preconditions;

import java.util.Set;

public class ClassClasspath extends AbstractClasspath {

    /**
     * 基于这个 Class 去加载
     */
    private Class clazz;
    private Location root = new Location(ClassPathResource.PREFIX, "/");

    public ClassClasspath(@NonNull Class clazz) {
        Preconditions.checkNotNull(clazz);
        this.clazz = clazz;
    }

    @Override
    public Resource findResource(String relativePath) {
        relativePath = Classpaths.getCanonicalFilePath(relativePath);
        return new ClassPathResource(relativePath, this.clazz);
    }

    @Override
    public Location getRoot() {
        return root;
    }

    @Override
    public Set<Location> allResources() {
        return null;
    }
}
