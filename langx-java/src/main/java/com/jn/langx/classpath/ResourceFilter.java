package com.jn.langx.classpath;

import com.jn.langx.io.resource.Location;
import com.jn.langx.util.function.Predicate;

/**
 * Filter predicate to determine which scanned resources should be returned.
 */
public interface ResourceFilter extends Predicate<Location> {
    /**
     * Return true if this resource should be included in the scan result.
     */
    @Override
    boolean test(Location resourceLocation);
}
