package com.jn.langx.lifecycle;

import java.util.EventListener;

public interface Lifecycle {
    void startup();

    void shutdown();

    /**
     * @return true if the component is starting or has been started.
     */
    boolean isRunning();

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

    boolean addEventListener(EventListener listener);

    boolean removeEventListener(EventListener listener);
}
