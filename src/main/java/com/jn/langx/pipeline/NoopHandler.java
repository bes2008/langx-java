package com.jn.langx.pipeline;

import com.jn.langx.annotation.Singleton;

@Singleton
public class NoopHandler implements Handler {

    private static final NoopHandler instance = new NoopHandler();

    private NoopHandler(){}

    public static NoopHandler getInstance() {
        return instance;
    }

    @Override
    public void inbound(HandlerContext ctx) {
        // NOOP
    }

    @Override
    public void outbound(HandlerContext ctx) {
        // NOOP
    }
}
