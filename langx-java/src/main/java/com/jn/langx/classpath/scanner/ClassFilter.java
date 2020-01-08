package com.jn.langx.classpath.scanner;

/**
 * Filter predicate to determine which scanned classes should be included.
 */
public interface ClassFilter {

    /**
     * Return true if this class should be included in the scan result.
     */
    boolean isMatch(Class<?> cls);
}
