package com.jn.langx.configuration;

public interface ConfigurationWriter<T extends Configuration> {

    /**
     * whether supports write or not
     *
     * @return true if supports, else false
     */
    boolean isSupportsWrite();

    /**
     * add a configuration
     *
     * @param configuration the configuration
     */
    void write(T configuration);

    /**
     * whether supports rewrite or not
     *
     * @return true if supports, else false
     */
    boolean isSupportsRewrite();

    /**
     * update a configuration
     *
     * @param configuration the configuration
     */
    void rewrite(T configuration);

    /**
     * whether supports remove or not
     *
     * @return true if supports, else false
     */
    boolean isSupportsRemove();

    /**
     * remove a configuration by id
     *
     * @param id the configuration id
     */
    void remove(String id);

}
