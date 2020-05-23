package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.InputStreamResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.collection.Collects;

import java.io.InputStream;
import java.util.Set;

public class InputStreamClasspath extends AbstractClasspath {

    private InputStreamResource resource;

    public InputStreamClasspath(String desc, InputStream inputStream) {
        resource = Resources.asInputStreamResource(inputStream, desc);
    }

    @Override
    public Resource findResource(String relativePath, boolean isClass) {
        relativePath = Classpaths.getPath(relativePath, isClass);
        if (relativePath.equals(resource.getDescription())) {
            return resource;
        }
        return null;
    }

    @Override
    public Location getRoot() {
        return null;
    }

    @Override
    public Set<Location> allResources() {
        return Collects.newHashSet(resource.getLocation());
    }
}
