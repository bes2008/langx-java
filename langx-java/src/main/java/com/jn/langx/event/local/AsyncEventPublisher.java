package com.jn.langx.event.local;

import com.jn.langx.event.CommonEventPublisher;

import java.util.concurrent.ExecutorService;

public class AsyncEventPublisher extends CommonEventPublisher {
    public AsyncEventPublisher(boolean parallelForListeners) {
        setDispatcher(new AsyncEventDispatcher(parallelForListeners));
    }

    public ExecutorService getExecutor() {
        return ((AsyncEventDispatcher) getDispatcher()).getExecutor();
    }

    public void setExecutor(ExecutorService executor) {
        ((AsyncEventDispatcher) this.getDispatcher()).setExecutor(executor);
    }
}
