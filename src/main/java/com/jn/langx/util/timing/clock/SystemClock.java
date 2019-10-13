package com.jn.langx.util.timing.clock;


/**
 * A clock that returns the time of the system / process.
 *
 * <p>This clock uses {@link System#currentTimeMillis()} for <i>absolute time</i>
 * and {@link System#nanoTime()} for <i>relative time</i>.
 *
 * <p>This SystemClock exists as a singleton instance.
 */
public class SystemClock implements Clock {

    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }

}
