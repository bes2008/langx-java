package com.jn.langx.propertyset;

import com.jn.langx.Nameable;

/**
 * Represents a set of properties and provides operations to access these properties.
 * This interface extends the Nameable interface, allowing the property set to have a name.
 * @since 5.3.8
 */
public interface PropertySet<SRC> extends Nameable {

    /**
     * A constant string used to identify environment variables.
     * @since 5.4.6
     */
    String ENV_VARS = "envVars";

    /**
     * A constant string used to identify system properties.
     * @since 5.4.6
     */
    String SYSTEM_PROPS = "systemProperties";

    /**
     * Gets the source object of this property set.
     * @return the source object of this property set
     */
    SRC getSource();

    /**
     * Checks if this property set contains the specified property.
     * @param name the name of the property to check
     * @return true if this property set contains the specified property; otherwise, false
     */
    boolean containsProperty(String name);

    /**
     * Gets the value of the specified property.
     * @param name the name of the property to get
     * @return the value of the specified property, or null if this property set does not contain the specified property
     */
    Object getProperty(String name);
}
