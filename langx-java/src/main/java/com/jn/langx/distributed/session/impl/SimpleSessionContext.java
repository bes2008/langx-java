package com.jn.langx.distributed.session.impl;

import com.jn.langx.distributed.session.SessionContext;

public class SimpleSessionContext implements SessionContext {
    private String sessionId;

    public SimpleSessionContext(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void setSessionId(String id) {
        this.sessionId = id;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }
}
