package com.jn.langx.classpath.scanner;

import java.util.List;

/**
 * copy from: org.avaje:avaje-classpath-scanner.3.1.1
 * <p>
 * Scans the class path for resources or classes.
 */
public interface ClassPathScanner {

    /**
     * Scan for file resources using the starting location and filter.
     *
     * @param location The path location from which the scan will start.
     * @param filter   The filter used to match resources.
     * @return The list of resources found that match our filter.
     */
    List<Resource> scanForResources(String location, ResourceFilter filter);

    /**
     * Scan of classes using the starting package and filter.
     *
     * @param location The package location from which the scan will start.
     * @param filter   The filter used to match classes.
     * @return The list of classes found that match our filter.
     */
    List<Class<?>> scanForClasses(String location, ClassFilter filter);
}
