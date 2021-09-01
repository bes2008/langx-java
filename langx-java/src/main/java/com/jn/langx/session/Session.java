package com.jn.langx.session;


import com.jn.langx.session.exception.InvalidSessionException;
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
    void setLastAccessTime(Date date);

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
    long getMaxInactiveInterval();

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
    void setMaxInactiveInterval(long maxIdleTimeInMillis);


    boolean isExpired();
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
