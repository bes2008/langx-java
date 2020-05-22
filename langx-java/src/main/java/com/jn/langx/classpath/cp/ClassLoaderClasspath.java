package com.jn.langx.classpath.cp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Preconditions;

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
}
