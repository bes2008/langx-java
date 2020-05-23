package com.jn.langx.classpath.cp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.classpath.scanner.ClassFilter;
import com.jn.langx.classpath.scanner.ResourceFilter;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Preconditions;

import java.util.List;
import java.util.Set;

public class ClassLoaderClasspath extends AbstractClasspath {
    private ClassLoader classLoader;

    public ClassLoaderClasspath(@NonNull ClassLoader loader) {
        Preconditions.checkNotNull(loader);
        this.classLoader = loader;
    }

    @Override
    public Resource findResource(String path, boolean isClass) {
        path = Classpaths.getPath(path, isClass);
        return new ClassPathResource(path, classLoader);
    }

    @Override
    public List<Resource> scanResources(String namespace, ResourceFilter filter) {
        return null;
    }

    @Override
    public List<ClassFile> scanClassFiles(String namespace, ClassFilter filter) {
        return null;
    }

    @Override
    public Set<Location> findResources(String namespace, ResourceFilter filter) {
        return null;
    }

    @Override
    public Set<Location> allResources() {
        return null;
    }
}
