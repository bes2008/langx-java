package com.jn.langx.lifecycle;

import com.jn.langx.event.EventListener;

/**
 * Listener.
 * A listener for Lifecycle events.
 */
interface StatefulEventListener extends EventListener {
    void lifecycleStarting(StatefulLifecycle lifecycle);

    void lifecycleStarted(StatefulLifecycle lifecycle);

    void lifecycleFailure(StatefulLifecycle lifecycle, Throwable cause);

    void lifecycleStopping(StatefulLifecycle lifecycle);

    void lifecycleStopped(StatefulLifecycle lifecycle);
}