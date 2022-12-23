package com.jn.langx.util.timing.clock;


/**
 * A clock that gives access to time. This clock returns two flavors of time:
 * <p>
 * <p><b>Absolute Time:</b> This refers to real world wall clock time, and it typically
 * derived from a system clock. It is subject to clock drift and inaccuracy, and can jump
 * if the system clock is adjusted.
 * <p>
 * <p><b>Relative Time:</b> This time advances at the same speed as the <i>absolute time</i>,
 * but the timestamps can only be referred to relative to each other. The timestamps have
 * no absolute meaning and cannot be compared across JVM processes. The source for the
 * timestamps is not affected by adjustments to the system clock, so it never jumps.
 */
public interface Clock {
    /**
     * Returns the current time tick.
     *
     * @return time tick in nanoseconds
     */
    long getTick();

    /**
     * Returns the current time in milliseconds.
     *
     * @return time in milliseconds
     */
    long getTime();
}
