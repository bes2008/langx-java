package com.jn.langx.util.timing.timer;

/**
 * @since 4.0.5
 * @param <TIMER>
 * @param <TIMEOUT>
 */
public interface TimeoutFactory<TIMER extends Timer, TIMEOUT extends Timeout> {
    TIMEOUT create(TIMER timer, TimerTask task, long deadline);
}
