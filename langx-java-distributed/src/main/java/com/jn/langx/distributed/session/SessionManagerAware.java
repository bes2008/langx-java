package com.jn.langx.distributed.session;

public interface SessionManagerAware {
    SessionManager getSessionManager();

    void setSessionManager(SessionManager sessionManager);
}
