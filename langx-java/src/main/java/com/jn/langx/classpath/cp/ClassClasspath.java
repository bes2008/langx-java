package com.jn.langx.classpath.cp;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Preconditions;

public class ClassClasspath implements Classpath {

    /**
     * 基于这个 Class 去加载
     */
    private Class clazz;

    public ClassClasspath(@NonNull Class clazz) {
        Preconditions.checkNotNull(clazz);
        this.clazz = clazz;
    }

    @Override
    public Resource find(String path, boolean isClass) {
        path = Classpaths.getPath(path, isClass);
        return new ClassPathResource(path, this.clazz);
    }
}
