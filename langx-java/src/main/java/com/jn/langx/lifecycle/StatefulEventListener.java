package com.jn.langx.lifecycle;

import com.jn.langx.event.EventListener;
/**
 * Listener.
 * A listener for Lifecycle events.
 * This interface is designed to listen for various events in the lifecycle of a Stateful object.
 * It extends the EventListener interface, marking it as an event listener.
 */
interface StatefulEventListener extends EventListener {
    /**
     * Called when the lifecycle is about to start.
     * This method is executed before the lifecycle starts, allowing pre-start operations to be performed.
     *
     * @param lifecycle The lifecycle object that is about to start.
     */
    void lifecycleStarting(StatefulLifecycle lifecycle);

    /**
     * Called when the lifecycle has started.
     * This method is executed after the lifecycle has successfully started, allowing post-start operations to be performed.
     *
     * @param lifecycle The lifecycle object that has already started.
     */
    void lifecycleStarted(StatefulLifecycle lifecycle);

    /**
     * Called when the lifecycle encounters a failure.
     * This method is executed when an exception occurs during the lifecycle, allowing error handling operations to be performed.
     *
     * @param lifecycle The lifecycle object that encountered a failure.
     * @param cause     The cause of the failure.
     */
    void lifecycleFailure(StatefulLifecycle lifecycle, Throwable cause);

    /**
     * Called when the lifecycle is about to stop.
     * This method is executed before the lifecycle stops, allowing pre-stop operations to be performed.
     *
     * @param lifecycle The lifecycle object that is about to stop.
     */
    void lifecycleStopping(StatefulLifecycle lifecycle);

    /**
     * Called when the lifecycle has stopped.
     * This method is executed after the lifecycle has successfully stopped, allowing post-stop operations to be performed.
     *
     * @param lifecycle The lifecycle object that has already stopped.
     */
    void lifecycleStopped(StatefulLifecycle lifecycle);
}
