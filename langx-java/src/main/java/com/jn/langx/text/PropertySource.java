package com.jn.langx.text;

import com.jn.langx.Named;

public interface PropertySource extends Named {

    /**
     * whether contains the specified property or not
     *
     * @param name
     * @return
     */
    boolean containsProperty(String name);

    /**
     * @param name the property name
     * @return null if not contains the specified name
     */
    String getProperty(String name);
}
