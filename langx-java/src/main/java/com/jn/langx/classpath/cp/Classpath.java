package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.Resource;

/**
 * <code>ClassPath</code> is an interface implemented by objects
 * representing a class search path.
 *
 * <p>The users can define a class implementing this interface so that
 * a class file is obtained from a non-standard source.
 */
public interface Classpath {

    /**
     * Returns the uniform resource locator (URL) of the class file
     * with the specified name.
     *
     * @param resourceLocation your resource location
     * @return null if the specified class file could not be found.
     */
    Resource findResource(String resourceLocation, boolean isClass);

    ClassFile findClassFile(String classname);
}
