package com.jn.langx.lifecycle;

/**
 * The Pauseable interface is used to provide a standard pause behavior.
 * It can be implemented by classes that need to have the ability to pause during execution.
 */
public interface Pauseable {
    /**
     * The pause method is used to implement the pause functionality.
     * When this method is called, the current activity or process should be paused.
     */
    void pause();
}
