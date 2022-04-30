package com.jn.langx.lifecycle;


import java.util.EventListener;

/**
 * Listener.
 * A listener for Lifecycle events.
 */
interface Listener extends EventListener {
    void lifeCycleStarting(Lifecycle event);

    void lifeCycleStarted(Lifecycle event);

    void lifeCycleFailure(Lifecycle event, Throwable cause);

    void lifeCycleStopping(Lifecycle event);

    void lifeCycleStopped(Lifecycle event);
}