package com.jn.langx.util.timing.timer;

public class HashedWheelTimeoutFactory implements TimeoutFactory<HashedWheelTimer, HashedWheelTimeout>{
    public static final HashedWheelTimeoutFactory INSTANCE = new HashedWheelTimeoutFactory();

    @Override
    public HashedWheelTimeout create(HashedWheelTimer timer, TimerTask task, long deadline) {
        return new HashedWheelTimeout(timer, task, deadline);
    }
}
