package com.jn.langx.distributed.session;

import com.jn.langx.event.EventListener;

public interface SessionListener extends EventListener<SessionEvent> {
    @Override
    void on(SessionEvent event);
}
