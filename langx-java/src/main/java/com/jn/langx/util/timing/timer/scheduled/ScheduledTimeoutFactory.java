package com.jn.langx.util.timing.timer.scheduled;

import com.jn.langx.util.timing.timer.TimeoutFactory;
import com.jn.langx.util.timing.timer.TimerTask;

/**
 * @since 4.6.4
 */
public class ScheduledTimeoutFactory implements TimeoutFactory<ScheduledExecutorTimer, ScheduledTimeout> {
    @Override
    public ScheduledTimeout create(ScheduledExecutorTimer timer, TimerTask task, long deadline) {
        return new ScheduledTimeout(timer, task, deadline);
    }
}
