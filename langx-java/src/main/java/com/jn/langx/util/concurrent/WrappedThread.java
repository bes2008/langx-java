package com.jn.langx.util.concurrent;

@Deprecated
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
        super(group, CommonTask.wrap(target));
    }

    public WrappedThread(Runnable target, String name) {
        super(CommonTask.wrap(target), name);
    }

    public WrappedThread(ThreadGroup group, Runnable target, String name) {
        super(group, CommonTask.wrap(target), name);
    }

    public WrappedThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, CommonTask.wrap(target), name, stackSize);
    }

}
