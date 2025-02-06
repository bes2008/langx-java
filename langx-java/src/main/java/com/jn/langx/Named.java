package com.jn.langx;

/**
 * The Named interface is used to define objects that have names.
 * Any class that implements this interface should provide an implementation for the getName method,
 * to return the name of the object.
 */
public interface Named {
    /**
     * Gets the name of the object.
     *
     * @return Returns the name of the object as a String.
     */
    String getName();
}
