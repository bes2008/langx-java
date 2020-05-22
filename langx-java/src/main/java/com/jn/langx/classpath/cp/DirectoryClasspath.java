package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.DirectoryBasedFileResourceLoader;
import com.jn.langx.io.resource.Resource;

public class DirectoryClasspath implements Classpath {
    private DirectoryBasedFileResourceLoader loader;

    public DirectoryClasspath(String directory) {
        loader = new DirectoryBasedFileResourceLoader(directory);
    }

    @Override
    public Resource find(String filepath, boolean isClass) {
        filepath = Classpaths.getPath(filepath, isClass);
        return loader.loadResource(filepath);
    }
}
