package com.jn.langx.session;

public interface SessionManagerAware {
    SessionManager getSessionManager();

    void setSessionManager(SessionManager sessionManager);
}
