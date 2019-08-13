package com.jn.langx.util.concurrent;

public class WrapedThread extends Thread {

    public WrapedThread() {
        super();
    }

    public WrapedThread(String name) {
        super(name);
    }

    public WrapedThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public WrapedThread(ThreadGroup group, Runnable target) {
        super(group, WrapedTasks.wrap(target));
    }

    public WrapedThread(Runnable target, String name) {
        super(WrapedTasks.wrap(target), name);
    }

    public WrapedThread(ThreadGroup group, Runnable target, String name) {
        super(group, WrapedTasks.wrap(target), name);
    }

    public WrapedThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, WrapedTasks.wrap(target), name, stackSize);
    }

}
