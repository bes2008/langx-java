package com.jn.langx.commandline;

/**
 * Interface for classes that want to be notified by Watchdog.
 * 
 * @see com.jn.langx.commandline.Watchdog
 *
 * @version $Id: TimeoutObserver.java 1556869 2014-01-09 16:51:11Z britter $
 */
public interface TimeoutObserver {

    /**
     * Called when the watchdog times out.
     * 
     * @param w the watchdog that timed out.
     */
    void timeoutOccured(Watchdog w);
}
