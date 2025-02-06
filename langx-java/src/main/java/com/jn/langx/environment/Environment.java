package com.jn.langx.environment;

import com.jn.langx.propertyset.PropertySet;

/**
 * The Environment interface is used to provide configuration information to the system.
 * It allows the system to access environment-specific properties, supporting the retrieval of individual property values and property sets.
 */
public interface Environment {

    /**
     * Retrieves the property value associated with the specified key.
     *
     * @param key the key of the property to retrieve
     * @return the value of the property if found; otherwise, null
     */
    String getProperty(String key);

    /**
     * Retrieves the property value associated with the specified key, returning a default value if the property does not exist.
     *
     * @param key the key of the property to retrieve
     * @param valueIfAbsent the default value to return if the property does not exist
     * @return the value of the property if found; otherwise, the specified default value
     */
    String getProperty(String key, String valueIfAbsent);

    /**
     * Retrieves a set of properties associated with the specified name.
     *
     * @param name the name of the property set to retrieve
     * @return the PropertySet object containing the properties
     *
     * @since 5.4.6
     */
    PropertySet getPropertySet(String name);
}
