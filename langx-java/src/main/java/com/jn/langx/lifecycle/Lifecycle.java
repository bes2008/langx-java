package com.jn.langx.lifecycle;

/**
 * The Lifecycle interface defines the lifecycle management for a component.
 * It includes two fundamental methods: startup and shutdown, which are used to start and stop the component, respectively.
 */
public interface Lifecycle{
    /**
     * Starts the component.
     * This method should complete the initialization work required for the component to start providing services.
     * It may include, but is not limited to, loading configurations, establishing connections, starting internal services, etc.
     */
    void startup();

    /**
     * Stops the component.
     * This method should complete the cleanup work required for the component to stop providing services.
     * It may include, but is not limited to, closing connections, releasing resources, stopping internal services, etc.
     * After this method is executed, the component should be in a state where it is not providing services, but resources have been safely released.
     */
    void shutdown();
}
