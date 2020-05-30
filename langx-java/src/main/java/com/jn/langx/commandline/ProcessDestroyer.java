package com.jn.langx.commandline;

/**
 * Destroys all registered {@link java.lang.Process} after a certain event,
 * typically when the VM exits
 * @see com.jn.langx.commandline.ShutdownHookProcessDestroyer
 *
 * @version $Id: ProcessDestroyer.java 1636056 2014-11-01 21:12:52Z ggregory $
 */
public interface ProcessDestroyer {

    /**
     * Returns {@code true} if the specified
     * {@link java.lang.Process} was
     * successfully added to the list of processes to be destroy.
     *
     * @param process
     *      the process to add
     * @return {@code true} if the specified
     *      {@link java.lang.Process} was
     *      successfully added
     */
    boolean add(Process process);

    /**
     * Returns {@code true} if the specified
     * {@link java.lang.Process} was
     * successfully removed from the list of processes to be destroy.
     *
     * @param process
     *            the process to remove
     * @return {@code true} if the specified
     *      {@link java.lang.Process} was
     *      successfully removed
     */
    boolean remove(Process process);

    /**
     * Returns the number of registered processes.
     *
     * @return the number of register process
     */
    int size();
}
