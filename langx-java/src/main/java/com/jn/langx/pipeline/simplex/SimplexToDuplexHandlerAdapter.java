package com.jn.langx.pipeline.simplex;

import com.jn.langx.pipeline.AbstractHandler;
import com.jn.langx.pipeline.HandlerContext;

public class SimplexToDuplexHandlerAdapter extends AbstractHandler {
    private SimplexHandler handler;

    public SimplexToDuplexHandlerAdapter(SimplexHandler handler){
        this.handler = handler;
    }

    @Override
    public void inbound(HandlerContext ctx) throws Throwable {
        Object result = handler.apply(ctx.getTarget());
        ctx.getCurrentValueHolder().set(result);
        super.inbound(ctx);
    }
}
