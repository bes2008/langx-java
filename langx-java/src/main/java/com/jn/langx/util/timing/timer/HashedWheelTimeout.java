package com.jn.langx.util.timing.timer;

import com.jn.langx.util.reflect.Reflects;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class HashedWheelTimeout implements Timeout, Runnable {
    static final int ST_INIT = 0;
    static final int ST_CANCELLED = 1;
    static final int ST_EXPIRED = 2;
    private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");

    protected final HashedWheelTimer timer;
    protected final TimerTask task;
    protected final long deadline;

    @SuppressWarnings({"unused", "FieldMayBeFinal", "RedundantFieldInitialization"})
    private volatile int state = ST_INIT;

    // remainingRounds will be calculated and set by Worker.transferTimeoutsToBuckets() before the
    // HashedWheelTimeout will be added to the correct HashedWheelBucket.
    long remainingRounds;

    // This will be used to chain timeouts in HashedWheelTimerBucket via a double-linked-list.
    // As only the workerThread will act on it there is no need for synchronization / volatile.
    HashedWheelTimeout next;
    HashedWheelTimeout prev;

    // The bucket to which the timeout was added
    HashedWheelBucket bucket;

    public HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
        this.timer = timer;
        this.task = task;
        this.deadline = deadline;
    }

    @Override
    public Timer timer() {
        return timer;
    }

    @Override
    public TimerTask task() {
        return task;
    }

    @Override
    public boolean cancel() {
        // only update the state it will be removed from HashedWheelBucket on next tick.
        if (!compareAndSetState(ST_INIT, ST_CANCELLED)) {
            return false;
        }
        // If a task should be canceled we put this to another queue which will be processed on each tick.
        // So this means that we will have a GC latency of max. 1 tick duration which is good enough. This way
        // we can make again use of our MpscLinkedQueue and so minimize the locking / overhead as much as possible.
        timer.cancelledTimeouts.add(this);
        return true;
    }

    void remove() {
        HashedWheelBucket bucket = this.bucket;
        if (bucket != null) {
            bucket.remove(this);
        } else {
            timer.pendingTimeouts.decrementAndGet();
        }
    }

    public boolean compareAndSetState(int expected, int state) {
        return STATE_UPDATER.compareAndSet(this, expected, state);
    }

    public int state() {
        return state;
    }

    @Override
    public boolean isCancelled() {
        return state() == ST_CANCELLED;
    }

    @Override
    public boolean isExpired() {
        return state() == ST_EXPIRED;
    }

    public void expire() {
        if (!compareAndSetState(ST_INIT, ST_EXPIRED)) {
            return;
        }
        /**
         * @since 4.0.5
         */
        try {
            timer.getTaskExecutor().execute(this);
        } catch (Throwable t) {
            if (HashedWheelTimer.logger.isWarnEnabled()) {
                HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
            }
        }
    }

    /**
     * @since 4.0.5
     */
    @Override
    public void run() {
        try {
            task.run(this);
        } catch (Throwable t) {
            if (HashedWheelTimer.logger.isWarnEnabled()) {
                HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
            }
        }
    }

    @Override
    public String toString() {
        final long currentTime = System.nanoTime();
        long remaining = deadline - currentTime + timer.startTime;

        StringBuilder buf = new StringBuilder(192)
                .append(Reflects.getSimpleClassName(this))
                .append('(')
                .append("deadline: ");
        if (remaining > 0) {
            buf.append(remaining)
                    .append(" ns later");
        } else if (remaining < 0) {
            buf.append(-remaining)
                    .append(" ns ago");
        } else {
            buf.append("now");
        }

        if (isCancelled()) {
            buf.append(", cancelled");
        }

        return buf.append(", task: ")
                .append(task())
                .append(')')
                .toString();
    }
}