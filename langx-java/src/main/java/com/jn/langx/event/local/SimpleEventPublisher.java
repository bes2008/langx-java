package com.jn.langx.event.local;

import com.jn.langx.event.CommonEventPublisher;

public class SimpleEventPublisher extends CommonEventPublisher {

    SimpleEventPublisher(){
        setDispatcher(SimpleEventDispatcher.INSTANCE);
    }
}
