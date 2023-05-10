package com.jn.langx.util.regexp;

/**
 * Protects against long running operations that happen between the register and unregister invocations.
 * Threads that invoke {@link #register(RegexpMatcher)}, but take too long to invoke the {@link #unregister(RegexpMatcher)} method
 * will be interrupted.
 * <p>
 * This is needed for Joni's {@link org.joni.Matcher#search(int, int, int)} method, because
 * it can end up spinning endlessly if the regular expression is too complex. Joni has checks
 * that for every 30k iterations it checks if the current thread is interrupted and if so
 * returns {@link RegexpMatcher#interrupt()}.
 */
public interface MatcherWatchdog {

    /**
     * Registers the current matcher and interrupts the this matcher
     * if the takes too long for this thread to invoke {@link #unregister(RegexpMatcher)}.
     *
     * @param matcher The matcher to register
     */
    void register(RegexpMatcher matcher);

    /**
     * @return The maximum allowed time in milliseconds for a thread to invoke {@link #unregister(RegexpMatcher)}
     * after {@link #register(RegexpMatcher)} has been invoked before this ThreadWatchDog starts to interrupting that thread.
     */
    long maxExecutionTimeInMillis();

    /**
     * Unregisters the current matcher and prevents it from being interrupted.
     *
     * @param matcher The matcher to unregister
     */
    void unregister(RegexpMatcher matcher);


}
