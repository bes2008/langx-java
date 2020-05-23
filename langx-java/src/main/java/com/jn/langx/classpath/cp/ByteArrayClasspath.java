package com.jn.langx.classpath.cp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.classpath.Classpaths;
import com.jn.langx.io.resource.ByteArrayResource;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.collection.Collects;

import java.util.Set;

public class ByteArrayClasspath extends AbstractClasspath {
    private ByteArrayResource resource;

    public ByteArrayClasspath(String desc, byte[] bytes) {
        resource = Resources.asByteArrayResource(bytes, desc);
    }

    @Override
    public Resource findResource(@NonNull String relativePath, boolean isClass) {
        relativePath = Classpaths.getPath(relativePath, isClass);
        if (relativePath.equals(resource.getPath())) {
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
