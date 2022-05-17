package com.jn.langx.event.local;

import com.jn.langx.event.CommonEventPublisher;

public class SimpleEventPublisher extends CommonEventPublisher {

    public SimpleEventPublisher() {
        setName("simple-" + counter.getAndIncrement());
        setDispatcher(SimpleEventDispatcher.INSTANCE);
    }
}
