package com.jn.langx.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogableThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(LogableThread.class);

    public LogableThread(Runnable target, String name) {
        super(target, name);
    }

    @Override
    public void run() {
        try {
            super.run();
        } catch (Throwable ex) {
            logger.warn(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
}
