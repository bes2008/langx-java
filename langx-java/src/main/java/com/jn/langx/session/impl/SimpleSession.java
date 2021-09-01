package com.jn.langx.session.impl;

import com.jn.langx.session.Session;
import com.jn.langx.util.collection.AbstractAttributable;

import java.util.Date;

/**
 * @since 3.7.0
 */
public class SimpleSession extends AbstractAttributable implements Session {
    private String id;
    private Date startTime;
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

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Override
    public long getMaxInactiveInterval() {
        return timeout;
    }

    @Override
    public void setMaxInactiveInterval(long maxIdleTimeInMillis) {
        this.timeout = maxIdleTimeInMillis;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public void invalidate() {
        this.expired = true;
    }
}
