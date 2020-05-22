package com.jn.langx.classpath.scanner.internal;


import com.jn.langx.classpath.scanner.ClassFilter;
import com.jn.langx.classpath.scanner.ResourceFilter;
import com.jn.langx.io.resource.Location;
import com.jn.langx.io.resource.Resource;

import java.util.List;

/**
 * Scanner for both resources and classes.
 */
public interface ResourceAndClassScanner {
    /**
     * Scans the classpath for resources under the specified location, starting with the specified prefix and ending with
     * the specified suffix.
     *
     * @param location  The location in the classpath to start searching. Subdirectories are also searched.
     * @param predicate The predicate used to match the resource names.
     * @return The resources that were found.
     */
    List<Resource> scanForResources(Location location, ResourceFilter predicate);

    /**
     * Scans the classpath for concrete classes under the specified package implementing this interface.
     * Non-instantiable abstract classes are filtered out.
     *
     * @param location  The location (package) in the classpath to start scanning.
     *                  Subpackages are also scanned.
     * @param predicate The predicate used to match against scanned classes.
     * @return The non-abstract classes that were found.
     */
    List<Class<?>> scanForClasses(Location location, ClassFilter predicate);
}
