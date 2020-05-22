package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.DirectoryBasedFileResourceLoader;
import com.jn.langx.io.resource.Resource;

import java.io.File;

public class DirectoryClasspath implements Classpath {
    private DirectoryBasedFileResourceLoader loader;

    public DirectoryClasspath(String directory) {
        loader = new DirectoryBasedFileResourceLoader(directory);
    }

    @Override
    public Resource find(String filepath, boolean isClass) {
        if (isClass) {
            filepath = filepath.replace('.', '/') + ".class";
        }
        return loader.loadResource(filepath);
    }
}
