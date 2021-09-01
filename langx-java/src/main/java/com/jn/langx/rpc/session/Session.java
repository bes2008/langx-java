package com.jn.langx.rpc.session;


import com.jn.langx.rpc.session.exception.ExpiredSessionException;
import com.jn.langx.rpc.session.exception.InvalidSessionException;
import com.jn.langx.util.collection.Attributable;

import java.util.Date;

/**
 * A {@code Session} is a stateful data context associated with a single Subject (user, daemon process,
 * etc) who interacts with a software system over a period of time.
 * <p/>
 * A {@code Session} is intended to be managed by the business tier and accessible via other
 * tiers without being tied to any given client technology.  This is a <em>great</em> benefit to Java
 * systems, since until now, the only viable session mechanisms were the
 * {@code javax.servlet.http.HttpSession} or Stateful Session EJB's, which many times
 * unnecessarily coupled applications to web or ejb technologies.
 *
 * @since 3.7.0
 */
public interface Session extends Attributable {

    /**
     * Returns the unique identifier assigned by the system upon session creation.
     * <p/>
     * All return values from this method are expected to have proper {@code toString()},
     * {@code equals()}, and {@code hashCode()} implementations. Good candidates for such
     * an identifier are {@link java.util.UUID UUID}s, {@link java.lang.Integer Integer}s, and
     * {@link java.lang.String String}s.
     *
     * @return The unique identifier assigned to the session upon creation.
     */
    String getId();

    /**
     * Returns the time the session was started; that is, the time the system created the instance.
     *
     * @return The time the system created the session.
     */
    Date getStartTime();

    /**
     * Returns the last time the application received a request or method invocation from the user associated
     * with this session.  Application calls to this method do not affect this access time.
     *
     * @return The time the user last interacted with the system.
     * @see #touch()
     */
    Date getLastAccessTime();

    /**
     * Returns the time in milliseconds that the session may remain idle before expiring.
     * <ul>
     * <li>A negative return value means the session will never expire.</li>
     * <li>A non-negative return value (0 or greater) means the session expiration will occur if idle for that
     * length of time.</li>
     * </ul>
     * <b>*Note:</b> if you are used to the {@code HttpSession}'s {@code getMaxInactiveInterval()} method, the scale on
     * this method is different: Shiro Sessions use millisecond values for timeout whereas
     * {@code HttpSession.getMaxInactiveInterval} uses seconds.  Always use millisecond values with Shiro sessions.
     *
     * @return the time in milliseconds the session may remain idle before expiring.
     * @throws InvalidSessionException if the session has been stopped or expired prior to calling this method.
     */
    long getTimeout() throws InvalidSessionException;

    /**
     * Sets the time in milliseconds that the session may remain idle before expiring.
     * <ul>
     * <li>A negative value means the session will never expire.</li>
     * <li>A non-negative value (0 or greater) means the session expiration will occur if idle for that
     * length of time.</li>
     * </ul>
     * <p/>
     * <b>*Note:</b> if you are used to the {@code HttpSession}'s {@code getMaxInactiveInterval()} method, the scale on
     * this method is different: Shiro Sessions use millisecond values for timeout whereas
     * {@code HttpSession.getMaxInactiveInterval} uses seconds.  Always use millisecond values with Shiro sessions.
     *
     * @param maxIdleTimeInMillis the time in milliseconds that the session may remain idle before expiring.
     * @throws InvalidSessionException if the session has been stopped or expired prior to calling this method.
     */
    void setTimeout(long maxIdleTimeInMillis) throws InvalidSessionException;


    /**
     * Explicitly updates the {@link #getLastAccessTime() lastAccessTime} of this session to the current time when
     * this method is invoked.  This method can be used to ensure a session does not time out.
     * <p/>
     * Most programmers won't use this method directly and will instead rely on the last access time to be updated
     * automatically as a result of an incoming web request or remote procedure call/method invocation.
     * <p/>
     * However, this method is particularly useful when supporting rich-client applications such as
     * Java Web Start app, Java or Flash applets, etc.  Although rare, it is possible in a rich-client
     * environment that a user continuously interacts with the client-side application without a
     * server-side method call ever being invoked.  If this happens over a long enough period of
     * time, the user's server-side session could time-out.  Again, such cases are rare since most
     * rich-clients frequently require server-side method invocations.
     * <p/>
     * In this example though, the user's session might still be considered valid because
     * the user is actively &quot;using&quot; the application, just not communicating with the
     * server. But because no server-side method calls are invoked, there is no way for the server
     * to know if the user is sitting idle or not, so it must assume so to maintain session
     * integrity.  This {@code touch()} method could be invoked by the rich-client application code during those
     * times to ensure that the next time a server-side method is invoked, the invocation will not
     * throw an {@link ExpiredSessionException ExpiredSessionException}.  In short terms, it could be used periodically
     * to ensure a session does not time out.
     * <p/>
     * How often this rich-client &quot;maintenance&quot; might occur is entirely dependent upon
     * the application and would be based on variables such as session timeout configuration,
     * usage characteristics of the client application, network utilization and application server
     * performance.
     *
     * @throws InvalidSessionException if this session has stopped or expired prior to calling this method.
     */
    void touch() throws InvalidSessionException;

    /**
     * Explicitly stops (invalidates) this session and releases all associated resources.
     * <p/>
     * If this session has already been authenticated (i.e. the {@code Subject} that
     * owns this session has logged-in), calling this method explicitly might have undesired side effects:
     * <p/>
     * It is common for a {@code Subject} implementation to retain authentication state in the
     * {@code Session}.  If the session
     * is explicitly stopped by application code by calling this method directly, it could clear out any
     * authentication state that might exist, thereby effectively &quot;unauthenticating&quot; the {@code Subject}.
     * <p/>
     * As such, you might consider {@link } the 'owning'
     * {@code Subject} instead of manually calling this method, as a log out is expected to stop the
     * corresponding session automatically, and also allows framework code to execute additional cleanup logic.
     *
     * @throws InvalidSessionException if this session has stopped or expired prior to calling this method.
     */
    void invalidate();

    @Override
    void setAttribute(String name, Object value);

    @Override
    Object getAttribute(String name);

    @Override
    boolean hasAttribute(String name);

    @Override
    void removeAttribute(String name);

    @Override
    Iterable<String> getAttributeNames();
}
