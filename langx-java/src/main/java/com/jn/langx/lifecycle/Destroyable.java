package com.jn.langx.lifecycle;

/**
 * The Destroyable interface is used to define objects that can be destroyed.
 * It is primarily used to ensure that classes implementing this interface provide a method to release resources they may hold.
 * For example, closing database connections, network connections, or releasing system resources, etc.
 * By implementing this interface, classes indicate that they have the ability to perform cleanup operations.
 */
public interface Destroyable {
    /**
     * The destroy method is used to release the resources held by the object.
     * This method should be called when the object is no longer needed to ensure that all resources are properly released.
     * It can include operations such as closing files, database connections, or resetting internal states, etc.
     */
    void destroy();
}
