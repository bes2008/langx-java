package com.jn.langx.distributed.session;

/**
 * @since 3.7.0
 */
public interface SessionContext {
    String getSessionId();
    void setSessionId(String id);
}
