package com.jn.langx.util.timing.timer;

/**
 * @since 4.0.5 支持检查是否重复
 */
public class DistinctHashedWheelTimeoutFactory implements TimeoutFactory<HashedWheelTimer,DistinctHashedWheelTimeout> {
    public static final DistinctHashedWheelTimeoutFactory INSTANCE = new DistinctHashedWheelTimeoutFactory();

    @Override
    public DistinctHashedWheelTimeout create(HashedWheelTimer timer, TimerTask task, long deadline) {
        return new DistinctHashedWheelTimeout((HashedWheelTimer) timer, task, deadline);
    }
}
