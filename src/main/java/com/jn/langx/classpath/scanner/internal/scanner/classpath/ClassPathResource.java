package com.jn.langx.classpath.scanner.internal.scanner.classpath;


import com.jn.langx.classpath.scanner.Resource;
import com.jn.langx.classpath.scanner.core.ClassPathScanException;
import com.jn.langx.util.io.IOs;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * A resource on the classpath.
 */
public class ClassPathResource implements Comparable<ClassPathResource>, Resource {
    /**
     * The location of the resource on the classpath.
     */
    private String location;

    /**
     * The ClassLoader to use.
     */
    private ClassLoader classLoader;

    /**
     * Creates a new ClassPathResource.
     *
     * @param location    The location of the resource on the classpath.
     * @param classLoader The ClassLoader to use.
     */
    public ClassPathResource(String location, ClassLoader classLoader) {
        this.location = location;
        this.classLoader = classLoader;
    }

    public String toString() {
        return location;
    }

    public String getLocation() {
        return location;
    }

    public String getLocationOnDisk() {
        URL url = getUrl();
        if (url == null) {
            throw new ClassPathScanException("Unable to location resource on disk: " + location);
        }
        try {
            return new File(URLDecoder.decode(url.getPath(), "UTF-8")).getAbsolutePath();
        } catch (UnsupportedEncodingException e) {
            throw new ClassPathScanException("Unknown encoding: UTF-8", e);
        }
    }

    /**
     * @return The url of this resource.
     */
    private URL getUrl() {
        return classLoader.getResource(location);
    }

    public String loadAsString(String encoding) {
        try {
            InputStream inputStream = classLoader.getResourceAsStream(location);
            if (inputStream == null) {
                throw new ClassPathScanException("Unable to obtain inputstream for resource: " + location);
            }
            Reader reader = new InputStreamReader(inputStream, Charset.forName(encoding));

            return IOs.toString(reader);
        } catch (IOException e) {
            throw new ClassPathScanException("Unable to load resource: " + location + " (encoding: " + encoding + ")", e);
        }
    }

    public byte[] loadAsBytes() {
        try {
            InputStream inputStream = classLoader.getResourceAsStream(location);
            if (inputStream == null) {
                throw new ClassPathScanException("Unable to obtain inputstream for resource: " + location);
            }
            return IOs.toByteArray(inputStream);
        } catch (IOException e) {
            throw new ClassPathScanException("Unable to load resource: " + location, e);
        }
    }

    public String getFilename() {
        return location.substring(location.lastIndexOf("/") + 1);
    }

    public boolean exists() {
        return getUrl() != null;
    }

    @SuppressWarnings({"RedundantIfStatement"})
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassPathResource that = (ClassPathResource) o;

        if (!location.equals(that.location)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    @SuppressWarnings("NullableProblems")
    public int compareTo(ClassPathResource o) {
        return location.compareTo(o.location);
    }
}
