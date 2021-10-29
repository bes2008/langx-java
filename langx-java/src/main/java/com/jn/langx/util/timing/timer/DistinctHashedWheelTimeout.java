package com.jn.langx.util.timing.timer;

import com.jn.langx.util.Objs;

/**
 * @since 4.0.5 支持检查是否重复
 */
public class DistinctHashedWheelTimeout extends HashedWheelTimeout {
    public DistinctHashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
        super(timer, task, deadline);
    }

    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof DistinctHashedWheelTimeout)) {
            return false;
        }
        DistinctHashedWheelTimeout that = (DistinctHashedWheelTimeout) obj;
        if (that.timer != this.timer) {
            return false;
        }
        return Objs.equals(this.task, that.task);
    }

    @Override
    public final int hashCode() {
        return this.task.hashCode();
    }
}
