package com.jn.langx.session.impl;

import com.jn.langx.session.*;
import com.jn.langx.session.exception.SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default business-tier implementation of .  All session CRUD operations are
 * delegated to an internal {@link SessionRepository}.
 *
 * @since 3.7.0
 */
public class DefaultSessionManager implements SessionManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultSessionManager.class);

    private SessionFactory sessionFactory;

    protected SessionRepository repository;

    public DefaultSessionManager() {
        this.sessionFactory = new SimpleSessionFactory();
        //    this.repository = new MemorySessionDAO();
    }

    @Override
    public Session createSession(SessionContext context) {
        return null;
    }

    @Override
    public Session getSession(String sessionId) throws SessionException {
        return repository.getById(sessionId);
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


}
