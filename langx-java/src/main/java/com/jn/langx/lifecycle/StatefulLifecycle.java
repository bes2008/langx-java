package com.jn.langx.lifecycle;

import com.jn.langx.event.EventListener;

public interface StatefulLifecycle extends Lifecycle{

    /**
     * @return true if the component has been started.
     * @see #isStarting()
     */
    boolean isStarted();

    /**
     * @return true if the component is starting.
     * @see #isStarted()
     */
    boolean isStarting();

    /**
     * @return true if the component is stopping.
     * @see #isStopped()
     */
    boolean isStopping();

    /**
     * @return true if the component has been stopped.
     * @see #isStopping()
     */
    boolean isStopped();

    /**
     * @return true if the component has failed to start or has failed to stop.
     */
    boolean isFailed();

    void addEventListener(EventListener listener);

    void removeEventListener(EventListener listener);
}
