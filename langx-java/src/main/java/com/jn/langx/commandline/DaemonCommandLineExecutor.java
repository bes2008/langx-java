package com.jn.langx.commandline;

/**
 * Runs daemon processes asynchronously. Callers are expected to register a {@link InstructionSequenceDestroyer} before executing
 * any processes.
 *
 * @since 2.5.0
 */
public class DaemonCommandLineExecutor extends DefaultCommandLineExecutor {

    /**
     * Factory method to create a thread waiting for the result of an asynchronous execution.
     *
     * @param runnable the runnable passed to the thread
     * @param name     the name of the thread
     * @return the thread
     */
    @Override
    protected Thread createThread(final Runnable runnable, final String name) {
        final Thread t = super.createThread(runnable, name);
        t.setDaemon(true);
        return t;
    }
}
