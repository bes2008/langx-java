package com.jn.langx.pipeline;

import com.jn.langx.annotation.Singleton;

@Singleton
public class NoopHandler extends AbstractHandler {

    private static NoopHandler instance;

    private NoopHandler() {
    }

    public static NoopHandler getInstance() {
        if (instance == null) {
            synchronized (NoopHandler.class) {
                if (instance == null) {
                    instance = new NoopHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public void inbound(HandlerContext ctx) throws Throwable {
        // NOOP
        super.inbound(ctx);
    }

    @Override
    public void outbound(HandlerContext ctx) throws Throwable {
        // NOOP
        super.outbound(ctx);
    }
}
