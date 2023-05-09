package com.jn.langx.event.local;

import com.jn.langx.event.DomainEvent;
import com.jn.langx.event.EventDispatcher;
import com.jn.langx.event.EventListener;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.concurrent.CommonTask;
import com.jn.langx.util.function.Consumer;

import java.util.concurrent.ExecutorService;
/**
 * @since 4.6.2
 */
@SuppressWarnings("ALL")
public class AsyncEventDispatcher implements EventDispatcher {
    private ExecutorService executor;
    /**
     * 多个 Listener时，是并行运行，还是串行运行;
     */
    private boolean parallel = false;

    public AsyncEventDispatcher(){

    }

    public AsyncEventDispatcher(boolean parallel){
        this.setParallel(parallel);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public boolean isParallel() {
        return parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    @Override
    public void dispatch(final DomainEvent event, final Iterable<EventListener> subscribers) {
        if (!parallel) {
            this.getExecutor().execute(CommonTask.wrap(new Runnable() {
                @Override
                public void run() {
                    SimpleEventDispatcher.INSTANCE.dispatch(event, subscribers);
                }
            }));
        } else {
            Collects.forEach(subscribers, new Consumer<EventListener>() {
                @Override
                public void accept(final EventListener eventListener) {
                    AsyncEventDispatcher.this.getExecutor().execute(CommonTask.wrap(new Runnable() {
                        @Override
                        public void run() {
                            eventListener.on(event);
                        }
                    }));
                }
            });
        }
    }
}
