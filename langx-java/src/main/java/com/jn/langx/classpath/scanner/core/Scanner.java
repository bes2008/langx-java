package com.jn.langx.classpath.scanner.core;


import com.jn.langx.classpath.scanner.ClassFilter;
import com.jn.langx.classpath.scanner.Resource;
import com.jn.langx.classpath.scanner.ResourceFilter;
import com.jn.langx.classpath.scanner.internal.EnvironmentDetection;
import com.jn.langx.classpath.scanner.internal.ResourceAndClassScanner;
import com.jn.langx.classpath.scanner.internal.scanner.classpath.ClassPathScanner;
import com.jn.langx.classpath.scanner.internal.scanner.filesystem.FileSystemScanner;
import com.jn.langx.exception.UnsupportedPlatformException;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Locations;
import com.jn.langx.util.collection.Collects;

import java.util.List;

/**
 * Scanner for Resources and Classes.
 */
public class Scanner implements com.jn.langx.classpath.scanner.ClassPathScanner {

    private final ResourceAndClassScanner resourceAndClassScanner;

    private final FileSystemScanner fileSystemScanner = new FileSystemScanner();

    public Scanner(ClassLoader classLoader) {
        if (EnvironmentDetection.isAndroid()) {
            throw new UnsupportedPlatformException();
        } else {
            resourceAndClassScanner = new ClassPathScanner(classLoader);
        }
    }

    /**
     * Scans this location for resources matching the given predicate.
     * <p>
     * The location can have a prefix of <code>filesystem:</code> or <code>classpath:</code> to determine
     * how to scan. If no prefix is used then classpath scan is the default.
     * </p>
     *
     * @param location  The location to start searching. Subdirectories are also searched.
     * @param predicate The predicate used to match resource names.
     * @return The resources that were found.
     */
    public List<Resource> scanForResources(Location location, ResourceFilter predicate) {
        Location l = Locations.parseLocation(location.getLocation());
        if(Locations.isFileLocation(location)){
            return fileSystemScanner.scanForResources(location, predicate);
        }
        if(Locations.isClasspathLocation(location)) {
            return resourceAndClassScanner.scanForResources(location, predicate);
        }
        return Collects.newArrayList();
    }

    /**
     * Scans this location for resources matching the given predicate.
     * <p>
     * The location can have a prefix of <code>filesystem:</code> or <code>classpath:</code> to determine
     * how to scan. If no prefix is used then classpath scan is the default.
     * </p>
     *
     * @param location  The location to start searching. Subdirectories are also searched.
     * @param predicate The predicate used to match resource names.
     * @return The resources that were found.
     */
    @Override
    public List<Resource> scanForResources(String location, ResourceFilter predicate) {
        return scanForResources(Locations.parseLocation(location), predicate);
    }

    /**
     * Scans the classpath for classes under the specified package matching the given predicate.
     *
     * @param location  The package in the classpath to start scanning. Subpackages are also scanned.
     * @param predicate The predicate used to match scanned classes.
     * @return The classes found matching the predicate
     */
    public List<Class<?>> scanForClasses(Location location, ClassFilter predicate) {
        return resourceAndClassScanner.scanForClasses(location, predicate);
    }

    /**
     * Scans the classpath for classes under the specified package matching the given predicate.
     *
     * @param location  The package in the classpath to start scanning. Subpackages are also scanned.
     * @param predicate The predicate used to match scanned classes.
     * @return The classes found matching the predicate
     */
    @Override
    public List<Class<?>> scanForClasses(String location, ClassFilter predicate) {
        Location l = Locations.parseLocation(location);
        return scanForClasses(l, predicate);
    }

}