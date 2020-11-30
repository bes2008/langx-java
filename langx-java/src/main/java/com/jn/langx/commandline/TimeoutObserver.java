package com.jn.langx.commandline;

/**
 * Interface for classes that want to be notified by Watchdog.
 *
 * @see com.jn.langx.commandline.Watchdog
 */
public interface TimeoutObserver {

    /**
     * Called when the watchdog times out.
     *
     * @param w the watchdog that timed out.
     */
    void onTimeout(Watchdog w);
}
