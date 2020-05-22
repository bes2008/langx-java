package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.InputStreamResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;

import java.io.InputStream;

public class InputStreamClasspath implements Classpath {

    private InputStreamResource resource;

    public InputStreamClasspath(String desc, InputStream inputStream) {
        resource = Resources.asInputStreamResource(inputStream, desc);
    }

    @Override
    public Resource find(String path, boolean isClass) {
        path = Classpaths.getPath(path, isClass);
        if (path.equals(resource.getDescription())) {
            return resource;
        }
        return null;
    }
}
