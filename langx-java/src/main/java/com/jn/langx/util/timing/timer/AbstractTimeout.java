package com.jn.langx.util.timing.timer;

import com.jn.langx.util.concurrent.executor.ImmediateExecutor;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @since 4.6.4
 */
public abstract class AbstractTimeout implements Timeout, Runnable {

    protected final Timer timer;
    protected final TimerTask task;
    protected final long deadline;
    private volatile int state = ST_INIT;
    private static final AtomicIntegerFieldUpdater<AbstractTimeout> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(AbstractTimeout.class, "state");

    protected AbstractTimeout(Timer timer, TimerTask task, long deadline) {
        this.timer = timer;
        this.task = task;
        this.deadline = deadline;
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

    /**
     * @return 返回true时，说明 该任务正在被执行，或者已执行完毕。 未被执行时，返回false
     */
    @Override
    public boolean isExpired() {
        return state() == ST_EXPIRED;
    }


    /**
     * @since 4.0.5
     * <p>
     * executeTask() -> timer.getTaskExecutor().execute(this) => 在 executor中执行 timeout.run()方法 => 在executor 中执行 TimerTask
     */
    public void executeTask() {
        Executor executor = timer.getTaskExecutor();
        if (executor == null) {
            executor = ImmediateExecutor.INSTANCE;
        }
        executor.execute(this);
    }

    /**
     * @since 4.0.5
     */
    @Override
    public void run() {
        try {
            task.run(this);
        } catch (Throwable t) {
            Logger logger = Loggers.getLogger(getClass());
            if (logger.isWarnEnabled()) {
                logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
            }
        }
    }


    @Override
    public Timer timer() {
        return timer;
    }

    @Override
    public TimerTask task() {
        return task;
    }


}
