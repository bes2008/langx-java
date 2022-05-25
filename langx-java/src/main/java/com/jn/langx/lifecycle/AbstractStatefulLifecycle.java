package com.jn.langx.lifecycle;


import com.jn.langx.event.EventListener;
import com.jn.langx.event.EventPublisher;
import com.jn.langx.event.EventPublisherAware;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.concurrent.lock.AutoLock;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.os.Uptime;
import org.slf4j.Logger;

import java.util.List;

/**
 * Basic implementation of the life cycle interface for components.
 */
@SuppressWarnings({"rawtypes"})
public abstract class AbstractStatefulLifecycle implements StatefulLifecycle, EventPublisherAware {
    private static final Logger logger = Loggers.getLogger(AbstractStatefulLifecycle.class);

    private String domain;
    private EventPublisher publisher;
    private final AutoLock lock = new AutoLock();
    private volatile State state = State.STOPPED;

    /**
     * Method to override to start the lifecycle
     *
     * @throws StopException If thrown, the lifecycle will immediately be stopped.
     */
    protected void doStart() throws Exception {
    }

    /**
     * Method to override to stop the lifecycle
     */
    protected void doStop() throws Exception {
    }


    @Override
    public EventPublisher getEventPublisher() {
        return this.publisher;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public final void startup() {
        AutoLock l = null;
        try {
            l = lock.lock();
            try {
                switch (state) {
                    case STARTED:
                        return;

                    case STARTING:
                    case STOPPING:
                        throw new IllegalStateException(getState().name());

                    default:
                        try {
                            setStarting();
                            doStart();
                            setStarted();
                        } catch (StopException e) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Unable to stop", e);
                            }
                            setStopping();
                            doStop();
                            setStopped();
                        }
                        break;
                }
            } catch (Throwable e) {
                setFailed(e);
                throw Throwables.wrapAsRuntimeException(e);
            }
        } finally {
            IOs.close(l);
        }
    }

    @Override
    public final void shutdown() {
        AutoLock l = null;
        try {
            l = lock.lock();
            try {
                switch (state) {
                    case STOPPED:
                        break;

                    case STARTING:
                    case STOPPING:
                        throw new IllegalStateException(getState().name());

                    default:
                        setStopping();
                        doStop();
                        setStopped();
                        break;
                }
            } catch (Throwable e) {
                setFailed(e);
                throw Throwables.wrapAsRuntimeException(e);
            }
        } finally {
            IOs.close(l);
        }
    }

    public boolean isRunning() {
        return this.state == State.STARTED || this.state == State.STARTING;
    }

    public boolean isStarted() {
        return state == State.STARTED;
    }

    public boolean isStarting() {
        return state == State.STARTING;
    }

    public boolean isStopping() {
        return state == State.STOPPING;
    }

    public boolean isStopped() {
        return state == State.STOPPED;
    }

    @Override
    public boolean isFailed() {
        return state == State.FAILED;
    }

    public void addEventListener(EventListener listener) {
        publisher.addEventListener(this.domain, listener);
    }

    public void removeEventListener(EventListener listener) {
        publisher.removeEventListener(this.domain, listener);
    }

    public State getState() {
        return this.state;
    }

    public static State getState(StatefulLifecycle lc) {
        if (lc instanceof AbstractStatefulLifecycle) {
            return ((AbstractStatefulLifecycle) lc).state;
        }
        if (lc.isStarting()) {
            return State.STARTING;
        }
        if (lc.isStarted()) {
            return State.STARTED;
        }
        if (lc.isStopping()) {
            return State.STOPPING;
        }
        if (lc.isStopped()) {
            return State.STOPPED;
        }
        return State.FAILED;
    }

    private void setStarted() {
        if (state == State.STARTING) {
            state = State.STARTED;
            if (logger.isDebugEnabled()) {
                logger.debug("STARTED @{}ms {}", Uptime.getUptime(), this);
            }
            List<EventListener> listeners = publisher.getListeners(this.domain);
            for (EventListener listener : listeners) {
                if (listener instanceof StatefulEventListener) {
                    ((StatefulEventListener) listener).lifecycleStarted(this);
                }
            }
        }
    }

    private void setStarting() {
        if (logger.isDebugEnabled()) {
            logger.debug("STARTING {}", this);
        }
        state = State.STARTING;
        List<EventListener> listeners = publisher.getListeners(this.domain);
        for (EventListener listener : listeners) {
            if (listener instanceof StatefulEventListener) {
                ((StatefulEventListener) listener).lifecycleStarting(this);
            }
        }
    }

    private void setStopping() {
        if (logger.isDebugEnabled()) {
            logger.debug("STOPPING {}", this);
        }
        state = State.STOPPING;
        List<EventListener> listeners = publisher.getListeners(this.domain);
        for (EventListener listener : listeners) {
            if (listener instanceof StatefulEventListener) {
                ((StatefulEventListener) listener).lifecycleStopping(this);
            }
        }
    }

    private void setStopped() {
        if (state == State.STOPPING) {
            state = State.STOPPED;
            if (logger.isDebugEnabled()) {
                logger.debug("STOPPED {}", this);
            }
            List<EventListener> listeners = publisher.getListeners(this.domain);
            for (EventListener listener : listeners) {
                if (listener instanceof StatefulEventListener) {
                    ((StatefulEventListener) listener).lifecycleStopped(this);
                }
            }
        }
    }

    private void setFailed(Throwable th) {
        state = State.FAILED;
        if (logger.isDebugEnabled()) {
            logger.warn("FAILED {}: {}", this, th, th);
        }
        List<EventListener> listeners = publisher.getListeners(this.domain);
        for (EventListener listener : listeners) {
            if (listener instanceof StatefulEventListener) {
                ((StatefulEventListener) listener).lifecycleFailure(this, th);
            }
        }
    }


    @Override
    public String toString() {
        String name = getClass().getSimpleName();
        if (Strings.isBlank(name) && getClass().getSuperclass() != null) {
            name = getClass().getSuperclass().getSimpleName();
        }
        return String.format("%s@%x{%s}", name, hashCode(), getState());
    }

    /**
     * An exception, which if thrown by doStart will immediately stop the component
     */
    public class StopException extends RuntimeException {
    }
}