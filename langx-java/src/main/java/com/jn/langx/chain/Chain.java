package com.jn.langx.chain;


import com.jn.langx.annotation.NonNull;

public interface Chain<REQ, RESP> {

    /**
     * Causes the next filter in the chain to be invoked, or if the calling filter is the last filter
     * in the chain, causes the resource at the end of the chain to be invoked.
     *
     * @param request  the request to pass along the chain.
     * @param response the response to pass along the chain.
     */
    void handle(REQ request, RESP response);


    void addHandler(@NonNull Handler<REQ, RESP> handler);

    ChainContext getContext();

    void setContext(ChainContext context);
}

