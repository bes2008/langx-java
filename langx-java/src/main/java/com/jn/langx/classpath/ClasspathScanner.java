package com.jn.langx.classpath;

import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;

import java.util.List;
import java.util.Set;

/**
 * Scans resources or classes from the classpath
 */
public interface ClasspathScanner {

    /**
     * Scan for file resources using the starting location and filter.
     *
     * @param namespace The relative location from which the scan will start.
     * @param isClass the namespace will converted by package name
     * @param filter    The filter used to match resources.
     * @return The list of resources found that match our filter.
     */
    List<Resource> scanResources(String namespace, boolean isClass, ResourceFilter filter);

    /**
     * Scan of classes using the starting package and filter.
     *
     * @param packageName The relative location from which the scan will start.
     * @param filter    The filter used to match classes.
     * @return The list of classes found that match our filter.
     */
    List<ClassFile> scanClassFiles(String packageName, ResourceFilter filter);


    Set<Location> scanResourceLocations(String namespace, ResourceFilter filter);

    Set<Location> allResources();

}
