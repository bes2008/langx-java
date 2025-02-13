package com.jn.langx.configuration;

import com.jn.langx.Nameable;
import com.jn.langx.event.EventPublisherAware;
import com.jn.langx.lifecycle.Initializable;
import com.jn.langx.lifecycle.Lifecycle;
import com.jn.langx.repository.Repository;

import java.util.Map;
/**
 * A generic interface for managing configurations, allowing for CRUD operations and synchronization with storage.
 * This interface extends Lifecycle, EventPublisherAware, Initializable, and Repository interfaces to inherit lifecycle management,
 * event publishing, initialization capabilities, and basic repository functionalities.
 *
 * @param <T>     The type of configuration, must extend Configuration.
 * @param <Loader> The type of configuration loader, must extend ConfigurationLoader.
 * @param <Writer> The type of configuration writer, must extend ConfigurationWriter.
 */
public interface ConfigurationRepository<T extends Configuration, Loader extends ConfigurationLoader<T>, Writer extends ConfigurationWriter<T>> extends Lifecycle, EventPublisherAware, Initializable, Repository<T, String>, Nameable {

    /**
     * Set a loader for loading configurations.
     *
     * @param loader The configuration loader.
     */
    void setConfigurationLoader(Loader loader);

    /**
     * Get the current configuration loader.
     *
     * @return The configuration loader.
     */
    Loader getConfigurationLoader();

    /**
     * Set a writer for writing configurations.
     *
     * @param writer The configuration writer.
     */
    void setConfigurationWriter(Writer writer);

    /**
     * Get the current configuration writer.
     *
     * @return The configuration writer.
     */
    Writer getConfigurationWriter();

    /**
     * Get a configuration bean by a specified id.
     *
     * @param id The id of the configuration.
     * @return The configuration corresponding to the given id.
     */
    T getById(String id);

    /**
     * Remove a configuration by id, synchronizing with storage by default.
     *
     * @param id The id of the configuration to remove.
     */
    void removeById(String id);

    /**
     * Remove a configuration by id, with an option to synchronize with storage.
     *
     * @param id   The id of the configuration to remove.
     * @param sync Whether to synchronize with storage.
     */
    void removeById(String id, boolean sync);

    /**
     * Add a configuration to the repository, synchronizing with storage by default.
     *
     * @param configuration The configuration to add.
     */
    void add(T configuration);

    /**
     * Add a configuration to the repository, with an option to synchronize with storage.
     *
     * @param configuration The configuration to add.
     * @param sync          Whether to synchronize with storage.
     * @return The added configuration.
     */
    T add(T configuration, boolean sync);

    /**
     * Update a configuration in the repository, synchronizing with storage by default.
     *
     * @param configuration The configuration to update.
     */
    void update(T configuration);

    /**
     * Update a configuration in the repository, with an option to synchronize with storage.
     *
     * @param configuration The configuration to update.
     * @param sync          Whether to synchronize with storage.
     */
    void update(T configuration, boolean sync);

    /**
     * Get all configurations in the repository.
     *
     * @return A map of all configurations, with their ids as keys.
     */
    Map<String, T> getAll();
}

