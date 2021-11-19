package com.jn.langx.pipeline;

import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

public class DebugHandler extends AbstractHandler {

    @Override
    public void inbound(HandlerContext ctx)throws Throwable {
        Logger logger = Loggers.getLogger(getClass());
        if (logger.isDebugEnabled()) {
            logger.debug("inbounding, context: {}", ctx.toString());
        }
        super.inbound(ctx);
    }

    @Override
    public void outbound(HandlerContext ctx) throws Throwable {
        Logger logger = Loggers.getLogger(getClass());
        if (logger.isDebugEnabled()) {
            logger.debug("outbounding, context: {}", ctx.toString());
        }
        super.outbound(ctx);
    }
}
