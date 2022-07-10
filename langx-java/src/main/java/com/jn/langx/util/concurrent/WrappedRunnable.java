package com.jn.langx.util.concurrent;

@Deprecated
public class WrappedRunnable extends WrappedTask implements Runnable {
    private Runnable task;

    public WrappedRunnable() {
    }

    public WrappedRunnable(Runnable runnable) {
        setTask(runnable);
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    @Override
    protected Object run0() throws Exception {
        if (task != null) {
            task.run();
        }
        return null;
    }

    @Override
    public void run() {
        try {
            runInternal();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
}
