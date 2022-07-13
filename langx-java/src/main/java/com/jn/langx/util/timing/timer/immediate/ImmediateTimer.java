package com.jn.langx.util.timing.timer.immediate;

import com.jn.langx.util.timing.timer.AbstractTimer;
import com.jn.langx.util.timing.timer.RunnableToTimerTaskAdapter;
import com.jn.langx.util.timing.timer.Timeout;
import com.jn.langx.util.timing.timer.TimerTask;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ImmediateTimer extends AbstractTimer {

    @Override
    public Timeout newTimeout(Runnable task, long delay, TimeUnit unit) {
        return newTimeout(new RunnableToTimerTaskAdapter(task), delay, unit);
    }

    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        long deadline = System.currentTimeMillis();
        final ImmediateTimeout timeout = new ImmediateTimeout(this, task, deadline);
        timeout.executeTask();
        return timeout;
    }

    @Override
    public Set<Timeout> stop() {
        return null;
    }


}
