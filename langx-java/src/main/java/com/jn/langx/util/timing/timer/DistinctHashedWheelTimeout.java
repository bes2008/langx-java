package com.jn.langx.util.timing.timer;

import com.jn.langx.util.Objs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since 4.0.5 支持检查是否重复
 */
public class DistinctHashedWheelTimeout extends HashedWheelTimeout {

    private static final ConcurrentHashMap<HashedWheelTimer, ConcurrentHashMap<DistinctHashedWheelTimeout, Integer>> RUNNING_TASKS = new ConcurrentHashMap<HashedWheelTimer, ConcurrentHashMap<DistinctHashedWheelTimeout, Integer>>();

    public DistinctHashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
        super(timer, task, deadline);
    }

    @Override
    protected void executeTask() {
        ConcurrentHashMap<DistinctHashedWheelTimeout, Integer> currentTimerRunningTasks = RUNNING_TASKS.get(timer);
        if (currentTimerRunningTasks == null) {
            RUNNING_TASKS.putIfAbsent(timer, new ConcurrentHashMap<DistinctHashedWheelTimeout, Integer>());
            currentTimerRunningTasks = RUNNING_TASKS.get(timer);
        }
        // 如果 taskExecutor 是ThreadPool时，这个代码可以避免加入到 queue里
        if (!currentTimerRunningTasks.containsKey(this)) {
            // 这里不能将该timeout 放到currentTimerRunningTasks
            super.executeTask();
        }
    }

    /**
     * @since 4.0.5
     */
    @Override
    public void run() {
        try {
            ConcurrentHashMap<DistinctHashedWheelTimeout, Integer> currentTimerRunningTasks = RUNNING_TASKS.get(timer);
            if (currentTimerRunningTasks == null) {
                RUNNING_TASKS.putIfAbsent(timer, new ConcurrentHashMap<DistinctHashedWheelTimeout, Integer>());
                currentTimerRunningTasks = RUNNING_TASKS.get(timer);
            }
            if (!currentTimerRunningTasks.containsKey(this)) {
                currentTimerRunningTasks.put(this, 1);
                task.run(this);
                currentTimerRunningTasks.remove(this);
            }
        } catch (Throwable t) {
            if (HashedWheelTimer.logger.isWarnEnabled()) {
                HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
            }
        }
    }

    @Override
    public final boolean equals(Object obj) {
        if (!(obj instanceof DistinctHashedWheelTimeout)) {
            return false;
        }
        DistinctHashedWheelTimeout that = (DistinctHashedWheelTimeout) obj;
        if(this==that){
            return true;
        }
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
