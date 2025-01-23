package com.jn.langx.propertyset;

import com.jn.langx.Nameable;

/**
 * @since 5.3.8
 */
public interface PropertySet<SRC> extends Nameable {

    /**
     * @since 5.4.6
     */
    String ENV_VARS = "envVars";

    /**
     * @since 5.4.6
     */
    String SYSTEM_PROPS = "systemProperties";

    SRC getSource();

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
