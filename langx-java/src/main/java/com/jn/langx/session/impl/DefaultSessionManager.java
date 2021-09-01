package com.jn.langx.session.impl;

import com.jn.langx.session.*;
import com.jn.langx.session.exception.SessionException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @since 3.7.0
 */
public class DefaultSessionManager implements SessionManager {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSessionManager.class);

    private SessionFactory sessionFactory;
    protected SessionRepository repository;
    private long defaultTimeout = TimeUnit.MINUTES.toMillis(30); // units:mills, 30 min

    public DefaultSessionManager() {
        this.sessionFactory = new SimpleSessionFactory();
        this.repository = new LocalSessionRepository();
    }


    @Override
    public Session createSession(SessionContext context) {
        Session session = sessionFactory.get(context);

        long maxInactiveInterval = session.getMaxInactiveInterval();
        if (maxInactiveInterval <= 0L) {
            session.setMaxInactiveInterval(getDefaultTimeout());
        }

        Date startTime = session.getStartTime();
        Preconditions.checkNotNull(startTime, "the start time is null");

        Date lastAccessTime = session.getLastAccessTime();
        if (lastAccessTime == null) {
            session.setLastAccessTime(new Date());
        }

        String sessionId = session.getId();
        Preconditions.checkNotEmpty(sessionId, "the session id is empty or null");

        repository.add(session);
        if (session instanceof SessionManagerAware) {
            ((SessionManagerAware) session).setSessionManager(this);
        }
        return session;
    }


    @Override
    public Session getSession(String sessionId) throws SessionException {
        if (Strings.isBlank(sessionId)) {
            return null;
        }
        Session session = repository.getById(sessionId);
        if (session != null && !session.isExpired()) {
            session.setLastAccessTime(new Date());
            if (session instanceof SessionManagerAware) {
                ((SessionManagerAware) session).setSessionManager(this);
            }
            return session;
        }
        return null;
    }

    public SessionRepository getRepository() {
        return repository;
    }

    public void setRepository(SessionRepository repository) {
        this.repository = repository;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setDefaultTimeout(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

}
