package com.jn.langx.classpath;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;

/**
 * <code>ClassPath</code> is an interface implemented by objects
 * representing a class search path.
 *
 * <p>The users can define a class implementing this interface so that
 * a class file is obtained from a non-standard source.
 */
public interface Classpath extends ClasspathScanner {

    /**
     * Returns the uniform resource locator (URL) of the class file
     * with the specified name.
     *
     * @param relativePath your resource location
     * @return null if the specified class file could not be found.
     */
    Resource findResource(@NonNull String relativePath, boolean isClass);

    /**
     * 根据 class name 在 root 下查找
     * @param classname the class name
     * @return the class file
     */
    ClassFile findClassFile(String classname);

    /**
     * root 位置，如果 root 是 null, 则为无root资源
     * @return root location
     */
    Location getRoot();
}
