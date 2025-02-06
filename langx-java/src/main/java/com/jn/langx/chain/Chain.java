package com.jn.langx.chain;


import com.jn.langx.annotation.NonNull;

/**
 * Represents a chain structure in a filter chain, where each filter processes a request and passes it to the next filter
 * until the final resource is reached and executed. This interface defines the method for executing the next step in the chain,
 * as well as managing the context and handlers in the chain.
 *
 * @param <REQ>  the request type
 * @param <RESP> the response type
 */
public interface Chain<REQ, RESP> {

    /**
     * Causes the next filter in the chain to be invoked, or if the calling filter is the last filter
     * in the chain, causes the resource at the end of the chain to be invoked.
     *
     * @param request  the request to pass along the chain.
     * @param response the response to pass along the chain.
     */
    void handle(REQ request, RESP response);

    /**
     * Adds a handler to the chain. A handler is responsible for processing requests and responses,
     * and deciding whether to pass the request to the next element in the chain.
     *
     * @param handler the handler to add, which cannot be null.
     */
    void addHandler(@NonNull Handler<REQ, RESP> handler);

    /**
     * Gets the context of the chain. The context contains information shared by all elements in the chain,
     * such as configuration information or objects shared by requests.
     *
     * @return the context of the chain
     */
    ChainContext getContext();

    /**
     * Sets the context of the chain. This method is typically called when initializing the chain
     * to provide a shared context for all elements in the chain.
     *
     * @param context the context to set
     */
    void setContext(ChainContext context);
}
