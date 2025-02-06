package com.jn.langx.util.collection;

/**
 * The DiffResult interface is used to represent the result of a difference check.
 * It provides a method to determine if there are differences.
 */
public interface DiffResult {
    /**
     * Checks if there are differences.
     *
     * @return true if there are differences; otherwise, false.
     */
    boolean hasDifference();
}
