package com.jn.langx.text;

import com.jn.langx.Nameable;
import com.jn.langx.Named;

public interface PropertySource extends Nameable {

    /**
     * whether contains the specified property or not
     *
     * @param name
     * @return a boolean
     */
    boolean containsProperty(String name);

    /**
     * @param name the property name
     * @return null if not contains the specified name
     */
    String getProperty(String name);
}
