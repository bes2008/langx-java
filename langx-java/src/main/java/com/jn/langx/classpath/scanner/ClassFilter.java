package com.jn.langx.classpath.scanner;

import com.jn.langx.util.function.Predicate;

/**
 * Filter predicate to determine which scanned classes should be included.
 */
public interface ClassFilter extends Predicate<Class<?>> {

    /**
     * Return true if this class should be included in the scan result.
     */
    boolean test(Class<?> clazz);
}
