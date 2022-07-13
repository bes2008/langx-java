package com.jn.langx.util.timing.timer.immediate;

import com.jn.langx.util.timing.timer.AbstractTimeout;
import com.jn.langx.util.timing.timer.Timer;
import com.jn.langx.util.timing.timer.TimerTask;

public class ImmediateTimeout extends AbstractTimeout {
    @Override
    public boolean cancel() {
        return false;
    }

    public ImmediateTimeout(Timer timer, TimerTask task, long deadline) {
        super(timer, task, deadline);
    }

    @Override
    public void executeTask() {
        if (!compareAndSetState(ST_INIT, ST_EXPIRED)) {
            return;
        }
        super.executeTask();
    }
}
