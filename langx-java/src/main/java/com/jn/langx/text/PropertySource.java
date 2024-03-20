package com.jn.langx.text;

import com.jn.langx.Nameable;

public interface PropertySource extends Nameable {

    /**
     * whether contains the specified property or not
     *
     * @return a boolean
     */
    boolean containsProperty(String name);

    /**
     * @param name the property name
     * @return null if not contains the specified name
     */
    Object getProperty(String name);
}
