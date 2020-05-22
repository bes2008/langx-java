package com.jn.langx.classpath.cp;

import javassist.ClassPool;
import javassist.NotFoundException;

import java.io.InputStream;
import java.net.URL;

/**
 * <code>ClassPath</code> is an interface implemented by objects
 * representing a class search path.
 * <code>ClassPool</code> uses those objects for reading class files.
 *
 * <p>The users can define a class implementing this interface so that
 * a class file is obtained from a non-standard source.
 *
 * @see ClassPool#insertClassPath(javassist.ClassPath)
 * @see ClassPool#appendClassPath(javassist.ClassPath)
 * @see ClassPool#removeClassPath(javassist.ClassPath)
 */
public interface Classpath {
    /**
     * Opens a class file.
     * This method may be called just to examine whether the class file
     * exists as well as to read the contents of the file.
     *
     * <p>This method can return null if the specified class file is not
     * found.  If null is returned, the next search path is examined.
     * However, if an error happens, this method must throw an exception
     * so that the search will be terminated.
     *
     * <p>This method should not modify the contents of the class file.
     *
     * @param classname a fully-qualified class name
     * @return the input stream for reading a class file
     * @see javassist.Translator
     */
    InputStream openClassfile(String classname) throws NotFoundException;

    /**
     * Returns the uniform resource locator (URL) of the class file
     * with the specified name.
     *
     * @param classname a fully-qualified class name.
     * @return null if the specified class file could not be found.
     */
    URL find(String classname);
}
