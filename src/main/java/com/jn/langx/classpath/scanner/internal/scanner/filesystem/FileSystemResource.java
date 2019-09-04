package com.jn.langx.classpath.scanner.internal.scanner.filesystem;


import com.jn.langx.classpath.scanner.Resource;
import com.jn.langx.classpath.scanner.core.ClassPathScanException;
import com.jn.langx.util.io.IOs;

import java.io.*;
import java.nio.charset.Charset;

/**
 * A resource on the filesystem.
 */
public class FileSystemResource implements Resource, Comparable<FileSystemResource> {
    /**
     * The location of the resource on the filesystem.
     */
    private File location;

    /**
     * Creates a new ClassPathResource.
     *
     * @param location The location of the resource on the filesystem.
     */
    public FileSystemResource(String location) {
        this.location = new File(location);
    }

    public String toString() {
        return location.toString();
    }

    /**
     * @return The location of the resource on the classpath.
     */
    public String getLocation() {
        return location.getPath().replace('\\', '/');
    }

    /**
     * Retrieves the location of this resource on disk.
     *
     * @return The location of this resource on disk.
     */
    public String getLocationOnDisk() {
        return location.getAbsolutePath();
    }

    /**
     * Loads this resource as a string.
     *
     * @param encoding The encoding to use.
     * @return The string contents of the resource.
     */
    public String loadAsString(String encoding) {
        try {
            InputStream inputStream = new FileInputStream(location);
            Reader reader = new InputStreamReader(inputStream, Charset.forName(encoding));

            return IOs.toString(reader);
        } catch (IOException e) {
            throw new ClassPathScanException("Unable to load filesystem resource: " + location.getPath() + " (encoding: " + encoding + ")", e);
        }
    }

    /**
     * Loads this resource as a byte array.
     *
     * @return The contents of the resource.
     */
    public byte[] loadAsBytes() {
        try {
            InputStream inputStream = new FileInputStream(location);
            return IOs.toByteArray(inputStream);
        } catch (IOException e) {
            throw new ClassPathScanException("Unable to load filesystem resource: " + location.getPath(), e);
        }
    }

    /**
     * @return The filename of this resource, without the path.
     */
    public String getFilename() {
        return location.getName();
    }

    @SuppressWarnings("NullableProblems")
    public int compareTo(FileSystemResource o) {
        return location.compareTo(o.location);
    }
}
