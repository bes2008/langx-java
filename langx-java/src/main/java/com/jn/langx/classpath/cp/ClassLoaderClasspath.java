package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resource;

public class ClassLoaderClasspath implements Classpath {
    private ClassLoader classLoader;

    public ClassLoaderClasspath(ClassLoader loader){
        this.classLoader = loader;
    }

    @Override
    public Resource find(String path, boolean isClass) {
        path = Classpaths.getPath(path, isClass);
        return new ClassPathResource(path, classLoader);
    }
}
