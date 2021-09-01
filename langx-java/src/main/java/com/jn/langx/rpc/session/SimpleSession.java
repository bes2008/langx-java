package com.jn.langx.rpc.session;

import com.jn.langx.rpc.session.exception.InvalidSessionException;
import com.jn.langx.util.collection.AbstractAttributable;

import java.util.Date;

/**
 * @since 3.7.0
 */
public class SimpleSession extends AbstractAttributable implements Session {
    private String id;
    private Date startTime;
    private Date stopTime;
    private Date lastAccessTime;
    private long timeout;
    private boolean expired;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public void touch() throws InvalidSessionException {
        this.lastAccessTime = new Date();
    }

    @Override
    public void invalidate() {
        this.expired = true;
    }
}
