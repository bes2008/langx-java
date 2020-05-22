package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.Resource;

public abstract class AbstractClasspath implements Classpath {
    @Override
    public ClassFile findClassFile(String classname) {
        Resource resource = findResource(classname, true);
        if (resource == null) {
            return null;
        }
        return new ResourceClassFile(resource);
    }
}
