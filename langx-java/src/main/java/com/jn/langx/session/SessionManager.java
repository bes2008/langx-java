package com.jn.langx.session;

import com.jn.langx.session.exception.SessionException;

/**
 * A SessionManager manages the creation, maintenance, and clean-up of all application
 * {@link Session}s.
 *
 * @since 3.7.0
 */
public interface SessionManager {

    String getDomain();

    /**
     * Starts a new session based on the specified contextual initialization data, which can be used by the underlying
     * implementation to determine how exactly to create the internal Session instance.
     * <p/>
     * This method is mainly used in framework development, as the implementation will often relay the argument
     * to an underlying {@link SessionFactory} which could use the context to construct the internal Session
     * instance in a specific manner.  This allows pluggable {@link Session Session} creation
     * logic by simply injecting a {@code SessionFactory} into the {@code SessionManager} instance.
     *
     * @param context the contextual initialization data that can be used by the implementation or underlying
     *                {@link SessionFactory} when instantiating the internal {@code Session} instance.
     * @return the newly created session.
     * @see SessionFactory#get(SessionContext)
     */
    Session createSession(SessionContext context);

    void setSessionFactory(SessionFactory sessionFactory);

    SessionFactory getSessionFactory();

    /**
     * Retrieves the session corresponding to the specified contextual data (such as a session ID if applicable), or
     * {@code null} if no Session could be found.  If a session is found but invalid (stopped or expired), a
     * {@link SessionException} will be thrown.
     *
     * @param id the Session id to use to look-up the Session
     * @return the {@code Session} instance corresponding to the given lookup key or {@code null} if no session
     * could be acquired.
     * @throws SessionException if a session was found but it was invalid (stopped/expired).
     */
    Session getSession(String id);

    void invalidate(Session session);
}
