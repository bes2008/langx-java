package com.jn.langx.classpath.cp;

import com.jn.langx.classpath.scanner.ClassFilter;
import com.jn.langx.classpath.scanner.ResourceFilter;
import com.jn.langx.io.resource.ByteArrayResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;

import java.util.List;
import java.util.Set;

public class ByteArrayClasspath extends AbstractClasspath {
    private ByteArrayResource resource;

    public ByteArrayClasspath(String desc, byte[] bytes) {
        resource = Resources.asByteArrayResource(bytes, desc);
    }

    @Override
    public Resource findResource(String path, boolean isClass) {
        path = Classpaths.getPath(path, isClass);
        if (path.equals(resource.getDescription())) {
            return resource;
        }
        return null;
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
