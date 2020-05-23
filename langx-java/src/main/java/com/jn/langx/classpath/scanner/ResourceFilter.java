package com.jn.langx.classpath.scanner;

import com.jn.langx.util.function.Predicate;

/**
 * Filter predicate to determine which scanned resources should be returned.
 */
public interface ResourceFilter extends Predicate<String> {
    /**
     * Return true if this resource should be included in the scan result.
     */
    @Override
    boolean test(String resource);
}
