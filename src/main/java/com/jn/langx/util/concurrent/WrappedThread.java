package com.jn.langx.util.concurrent;

public class WrappedThread extends Thread {

    public WrappedThread() {
        super();
    }

    public WrappedThread(String name) {
        super(name);
    }

    public WrappedThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public WrappedThread(ThreadGroup group, Runnable target) {
        super(group, WrappedTasks.wrap(target));
    }

    public WrappedThread(Runnable target, String name) {
        super(WrappedTasks.wrap(target), name);
    }

    public WrappedThread(ThreadGroup group, Runnable target, String name) {
        super(group, WrappedTasks.wrap(target), name);
    }

    public WrappedThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, WrappedTasks.wrap(target), name, stackSize);
    }

}
