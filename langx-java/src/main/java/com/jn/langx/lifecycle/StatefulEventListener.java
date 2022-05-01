package com.jn.langx.lifecycle;

import com.jn.langx.event.EventListener;

/**
 * Listener.
 * A listener for Lifecycle events.
 */
interface StatefulEventListener extends EventListener {
    void lifecycleStarting(Lifecycle event);

    void lifecycleStarted(Lifecycle event);

    void lifecycleFailure(Lifecycle event, Throwable cause);

    void lifecycleStopping(Lifecycle event);

    void lifecycleStopped(Lifecycle event);
}