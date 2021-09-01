package com.jn.langx.rpc.session;

import com.jn.langx.factory.Factory;

/**
 * @since 3.7.0
 */
public interface SessionFactory extends Factory<SessionContext, Session> {
    @Override
    Session get(SessionContext ctx);
}
