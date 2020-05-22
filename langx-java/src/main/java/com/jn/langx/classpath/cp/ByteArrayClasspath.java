package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.ByteArrayResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;

public class ByteArrayClasspath implements Classpath {
    private ByteArrayResource resource;

    public ByteArrayClasspath(String desc, byte[] bytes) {
        resource = Resources.asByteArrayResource(bytes, desc);
    }

    @Override
    public Resource find(String path, boolean isClass) {
        if (isClass) {
            path = path.replace('.', '/') + ".class";
        }
        if (path.equals(resource.getDescription())) {
            return resource;
        }
        return null;
    }
}
