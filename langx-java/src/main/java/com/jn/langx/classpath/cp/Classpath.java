package com.jn.langx.classpath.cp;

import com.jn.langx.io.resource.Location;

import java.net.URL;

/**
 * <code>ClassPath</code> is an interface implemented by objects
 * representing a class search path.
 *
 * <p>The users can define a class implementing this interface so that
 * a class file is obtained from a non-standard source.
 */
public interface Classpath {

    Location getLocation();

    void setLocation(Location location);

    /**
     * Returns the uniform resource locator (URL) of the class file
     * with the specified name.
     *
     * @param resourceLocation a fully-qualified class name.
     * @return null if the specified class file could not be found.
     */
    URL find(String resourceLocation);
}
