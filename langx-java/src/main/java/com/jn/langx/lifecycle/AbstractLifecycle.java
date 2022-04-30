package com.jn.langx.lifecycle;


import com.jn.langx.util.Strings;
import com.jn.langx.util.concurrent.lock.AutoLock;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.os.Uptime;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Basic implementation of the life cycle interface for components.
 */
public abstract class AbstractLifecycle implements Lifecycle {
    private static final Logger logger = Loggers.getLogger(AbstractLifecycle.class);

    enum State {
        STOPPED,
        STARTING,
        STARTED,
        STOPPING,
        FAILED
    }

    public static final String STOPPED = State.STOPPED.toString();
    public static final String FAILED = State.FAILED.toString();
    public static final String STARTING = State.STARTING.toString();
    public static final String STARTED = State.STARTED.toString();
    public static final String STOPPING = State.STOPPING.toString();

    private final List<EventListener> _eventListener = new CopyOnWriteArrayList<EventListener>();
    private final AutoLock _lock = new AutoLock();
    private volatile State _state = State.STOPPED;

    /**
     * Method to override to start the lifecycle
     *
     * @throws StopException If thrown, the lifecycle will immediately be stopped.
     * @throws Exception     If there was a problem starting. Will cause a transition to FAILED state
     */
    protected void doStart() throws Exception {
    }

    /**
     * Method to override to stop the lifecycle
     *
     * @throws Exception If there was a problem stopping. Will cause a transition to FAILED state
     */
    protected void doStop() throws Exception {
    }

    @Override
    public final void startup() {
        AutoLock l = null;
        try {
            l = _lock.lock();
            try {
                switch (_state) {
                    case STARTED:
                        return;

                    case STARTING:
                    case STOPPING:
                        throw new IllegalStateException(getState());

                    default:
                        try {
                            setStarting();
                            doStart();
                            setStarted();
                        } catch (StopException e) {
                            if (logger.isDebugEnabled())
                                logger.debug("Unable to stop", e);
                            setStopping();
                            doStop();
                            setStopped();
                        }
                }
            } catch (Throwable e) {
                setFailed(e);
                throw new RuntimeException(e);
            }
        } finally {
            IOs.close(l);
        }
    }

    @Override
    public final void shutdown() {
        AutoLock l = null;
        try {
            l = _lock.lock();
            try {
                switch (_state) {
                    case STOPPED:
                        return;

                    case STARTING:
                    case STOPPING:
                        throw new IllegalStateException(getState());

                    default:
                        setStopping();
                        doStop();
                        setStopped();
                }
            } catch (Throwable e) {
                setFailed(e);
                throw new RuntimeException(e);
            }
        } finally {
            IOs.close(l);
        }
    }

    public boolean isRunning() {
        final State state = _state;
        switch (state) {
            case STARTED:
            case STARTING:
                return true;
            default:
                return false;
        }
    }

    public boolean isStarted() {
        return _state == State.STARTED;
    }

    public boolean isStarting() {
        return _state == State.STARTING;
    }

    public boolean isStopping() {
        return _state == State.STOPPING;
    }

    public boolean isStopped() {
        return _state == State.STOPPED;
    }

    @Override
    public boolean isFailed() {
        return _state == State.FAILED;
    }

    public List<EventListener> getEventListeners() {
        return _eventListener;
    }

    public void setEventListeners(Collection<EventListener> eventListeners) {
        for (EventListener l : _eventListener) {
            if (!eventListeners.contains(l))
                removeEventListener(l);
        }

        for (EventListener l : eventListeners) {
            if (!_eventListener.contains(l))
                addEventListener(l);
        }
    }

    public boolean addEventListener(EventListener listener) {
        if (_eventListener.contains(listener))
            return false;
        _eventListener.add(listener);
        return true;
    }

    public boolean removeEventListener(EventListener listener) {
        return _eventListener.remove(listener);
    }

    public String getState() {
        return _state.toString();
    }

    public static String getState(Lifecycle lc) {
        if (lc instanceof AbstractLifecycle)
            return ((AbstractLifecycle) lc)._state.toString();
        if (lc.isStarting())
            return State.STARTING.toString();
        if (lc.isStarted())
            return State.STARTED.toString();
        if (lc.isStopping())
            return State.STOPPING.toString();
        if (lc.isStopped())
            return State.STOPPED.toString();
        return State.FAILED.toString();
    }

    private void setStarted() {
        if (_state == State.STARTING) {
            _state = State.STARTED;
            if (logger.isDebugEnabled())
                logger.debug("STARTED @{}ms {}", Uptime.getUptime(), this);
            for (EventListener listener : _eventListener)
                if (listener instanceof Listener)
                    ((Listener) listener).lifeCycleStarted(this);
        }
    }

    private void setStarting() {
        if (logger.isDebugEnabled())
            logger.debug("STARTING {}", this);
        _state = State.STARTING;
        for (EventListener listener : _eventListener)
            if (listener instanceof Listener)
                ((Listener) listener).lifeCycleStarting(this);
    }

    private void setStopping() {
        if (logger.isDebugEnabled())
            logger.debug("STOPPING {}", this);
        _state = State.STOPPING;
        for (EventListener listener : _eventListener)
            if (listener instanceof Listener)
                ((Listener) listener).lifeCycleStopping(this);
    }

    private void setStopped() {
        if (_state == State.STOPPING) {
            _state = State.STOPPED;
            if (logger.isDebugEnabled())
                logger.debug("STOPPED {}", this);
            for (EventListener listener : _eventListener)
                if (listener instanceof Listener)
                    ((Listener) listener).lifeCycleStopped(this);
        }
    }

    private void setFailed(Throwable th) {
        _state = State.FAILED;
        if (logger.isDebugEnabled())
            logger.warn("FAILED {}: {}", this, th, th);
        for (EventListener listener : _eventListener) {
            if (listener instanceof Listener)
                ((Listener) listener).lifeCycleFailure(this, th);
        }
    }

    /**
     * @deprecated this class is redundant now that {@link Listener} has default methods.
     */
    @Deprecated
    public abstract static class AbstractLifecycleListener implements Listener {
        @Override
        public void lifeCycleFailure(Lifecycle event, Throwable cause) {
        }

        @Override
        public void lifeCycleStarted(Lifecycle event) {
        }

        @Override
        public void lifeCycleStarting(Lifecycle event) {
        }

        @Override
        public void lifeCycleStopped(Lifecycle event) {
        }

        @Override
        public void lifeCycleStopping(Lifecycle event) {
        }
    }

    @Override
    public String toString() {
        String name = getClass().getSimpleName();
        if (Strings.isBlank(name) && getClass().getSuperclass() != null)
            name = getClass().getSuperclass().getSimpleName();
        return String.format("%s@%x{%s}", name, hashCode(), getState());
    }

    /**
     * An exception, which if thrown by doStart will immediately stop the component
     */
    public class StopException extends RuntimeException {
    }
}