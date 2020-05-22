package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.ByteArrayResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;

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
}
