package com.jn.langx.lifecycle;

import com.jn.langx.event.EventListener;
public interface StatefulLifecycle extends Lifecycle{
    /**
     * Checks if the component is in the process of starting or has already started.
     *
     * @return true if the component is starting or has been started.
     */
    boolean isRunning();

    /**
     * Checks if the component has already started.
     *
     * @return true if the component has been started.
     * @see #isStarting()
     */
    boolean isStarted();

    /**
     * Checks if the component is in the process of starting.
     *
     * @return true if the component is starting.
     * @see #isStarted()
     */
    boolean isStarting();

    /**
     * Checks if the component is in the process of stopping.
     *
     * @return true if the component is stopping.
     * @see #isStopped()
     */
    boolean isStopping();

    /**
     * Checks if the component has already stopped.
     *
     * @return true if the component has been stopped.
     * @see #isStopping()
     */
    boolean isStopped();

    /**
     * Checks if the component has failed to start or stop.
     *
     * @return true if the component has failed to start or stop.
     */
    boolean isFailed();

    /**
     * Adds an event listener to listen for state changes of the component.
     *
     * @param listener The event listener to be added.
     */
    void addEventListener(EventListener listener);

    /**
     * Removes an event listener from listening for state changes of the component.
     *
     * @param listener The event listener to be removed.
     */
    void removeEventListener(EventListener listener);
}

