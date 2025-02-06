package com.jn.langx.configuration;

/**
 * Interface for configuration management
 * This interface is used to define the basic operations of configuration, mainly including obtaining and setting configuration identifiers
 * When using this interface, it is recommended to override the equals and hashCode methods to ensure the correctness and efficiency of configuration comparison and storage
 */
public interface Configuration {
    /**
     * Gets the unique identifier of the configuration
     * This method is used to uniquely identify a configuration, which is the basis for configuration comparison and storage
     *
     * @return the unique identifier of the configuration
     */
    String getId();

    /**
     * Sets the unique identifier of the configuration
     * This method is used to set the unique identifier of a configuration, which is the basis for configuration comparison and storage
     *
     * @param id the unique identifier of the configuration
     */
    void setId(String id);
}
