package com.jn.langx.classpath.cp;

import com.jn.langx.classpath.scanner.ClassFilter;
import com.jn.langx.classpath.scanner.ResourceFilter;
import com.jn.langx.io.resource.DirectoryBasedFileResourceLoader;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;

import java.util.List;
import java.util.Set;

public class DirectoryClasspath extends AbstractClasspath {
    private DirectoryBasedFileResourceLoader loader;

    public DirectoryClasspath(String directory) {
        loader = new DirectoryBasedFileResourceLoader(directory);
    }

    @Override
    public Resource findResource(String filepath, boolean isClass) {
        filepath = Classpaths.getPath(filepath, isClass);
        return loader.loadResource(filepath);
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
