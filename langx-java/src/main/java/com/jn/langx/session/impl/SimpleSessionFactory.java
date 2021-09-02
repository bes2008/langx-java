package com.jn.langx.session.impl;


import com.jn.langx.session.Session;
import com.jn.langx.session.SessionContext;
import com.jn.langx.session.SessionFactory;
import com.jn.langx.session.SessionIdGenerator;

import java.util.Date;

/**
 * {@code SessionFactory} implementation that generates {@link SimpleSession} instances.
 * 创建session
 *
 * @since 3.7.0
 */
public class SimpleSessionFactory implements SessionFactory {
    private SessionIdGenerator<SessionContext> idGenerator;

    /**
     * Creates a new {@link SimpleSession SimpleSession} instance retaining the context's
     * {@link SessionContext} if one can be found.
     *
     * @param context the initialization data to be used during {@link Session} creation.
     * @return a new {@link SimpleSession SimpleSession} instance
     */
    private Session createSession(SessionContext context) {
        SimpleSession session = new SimpleSession();
        String id = idGenerator.get(context);
        context.setSessionId(id);
        session.setId(id);
        session.setStartTime(new Date());
        return session;
    }

    @Override
    public Session get(SessionContext ctx) {
        return createSession(ctx);
    }

    @Override
    public SessionIdGenerator<SessionContext> getIdGenerator() {
        return idGenerator;
    }

    @Override
    public void setIdGenerator(SessionIdGenerator<SessionContext> idGenerator) {
        this.idGenerator = idGenerator;
    }
}
