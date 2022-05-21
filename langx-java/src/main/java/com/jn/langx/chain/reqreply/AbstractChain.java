package com.jn.langx.chain.reqreply;

import com.jn.langx.chain.ChainContext;

public abstract class AbstractChain<REQ, RESP> implements Chain<REQ, RESP> {
    private ChainContext context;

    @Override
    public ChainContext getContext() {
        return context;
    }

    @Override
    public void setContext(ChainContext context) {
        this.context = context;
    }
}
