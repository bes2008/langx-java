package com.jn.langx.event.local;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.util.concurrent.WrappedRunable;

import java.util.concurrent.ExecutorService;

public class AsyncEventPublisher extends SimpleEventPublisher {
    private ExecutorService executor;

    @Override
    public void publish(final DomainEvent event) {
        executor.execute(new WrappedRunable(new Runnable() {
            @Override
            public void run() {
                AsyncEventPublisher.super.publish(event);
            }
        }));
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}
