package com.jn.langx.util.concurrent;

public class WrappedRunable extends WrappedTask implements Runnable {
    private Runnable task;

    public WrappedRunable() {
    }

    public WrappedRunable(Runnable runnable) {
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
