package com.jn.langx.util.timing.timer;

public class RunnableToTimerTaskAdapter implements TimerTask {
    private Runnable t;

    public RunnableToTimerTaskAdapter(Runnable r) {
        this.t = r;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        if (!timeout.isCancelled()) {
            t.run();
        }
    }
}
