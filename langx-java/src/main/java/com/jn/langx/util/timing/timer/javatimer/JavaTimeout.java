package com.jn.langx.util.timing.timer.javatimer;

import com.jn.langx.util.timing.timer.AbstractTimeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;

public class JavaTimeout extends AbstractTimeout {

    private java.util.TimerTask taskRef;

    protected JavaTimeout(Timer timer, TimerTask task, long deadline) {
        super(timer, task, deadline);
    }

    void setTaskRef(java.util.TimerTask taskRef) {
        this.taskRef = taskRef;
    }

    @Override
    public void executeTask() {
        if (!compareAndSetState(ST_INIT, ST_EXPIRED)) {
            return;
        }
        super.executeTask();
    }

    @Override
    public boolean cancel() {
        return taskRef.cancel();
    }
}
