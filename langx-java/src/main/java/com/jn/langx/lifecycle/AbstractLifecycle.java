package com.jn.langx.lifecycle;

import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractLifecycle extends AbstractInitializable implements Lifecycle {
    private Logger logger = Loggers.getLogger(getClass());
    private volatile boolean running;
    private String name;
    private final ReentrantLock lifecycleLock = new ReentrantLock();

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final boolean isRunning() {
        this.lifecycleLock.lock();
        try {
            return this.running;
        } finally {
            this.lifecycleLock.unlock();
        }
    }

    public final void startup() {
        this.lifecycleLock.lock();
        try {
            init();
            if (!this.running) {
                this.doStart();
                this.running = true;
                if (logger.isInfoEnabled()) {
                    logger.info("started " + this);
                }
            }
        } finally {
            this.lifecycleLock.unlock();
        }
    }

    public final void shutdown() {
        this.lifecycleLock.lock();
        try {
            if (this.running) {
                this.doStop();
                this.running = false;
                if (logger.isInfoEnabled()) {
                    logger.info("stopped " + this);
                }
            }
        } finally {
            this.lifecycleLock.unlock();
        }
    }

    public final void stopAndExecute(Runnable callback) {
        this.lifecycleLock.lock();
        try {
            this.shutdown();
            callback.run();
        } finally {
            this.lifecycleLock.unlock();
        }
    }

    /**
     * Subclasses must implement this method with the start behavior.
     * This method will be invoked while holding the {@link #lifecycleLock}.
     */
    protected abstract void doStart();

    /**
     * Subclasses must implement this method with the stop behavior.
     * This method will be invoked while holding the {@link #lifecycleLock}.
     */
    protected abstract void doStop();

}
