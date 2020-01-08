package com.jn.langx.classpath.scanner;

/**
 * A loadable resource.
 */
public interface Resource {

    /**
     * Return the location of the resource on the classpath (path and filename).
     */
    String getLocation();

    /**
     * Return the location of this resource on disk.
     */
    String getLocationOnDisk();

    /**
     * Return the content of this resource as a string.
     *
     * @param encoding The encoding to use.
     * @return The string contents of the resource.
     */
    String loadAsString(String encoding);

    /**
     * Return the context of this resource as a byte array.
     */
    byte[] loadAsBytes();

    /**
     * Return the filename of this resource, without the path.
     */
    String getFilename();
}
