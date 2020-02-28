package com.jn.langx.pipeline;

import com.jn.langx.annotation.Prototype;

@Prototype
public interface Pipeline<T> {
    void addFirst(Handler handler);

    void addLast(Handler handler);

    HeadHandlerContext getHead();

    /**
     * clear all handlers
     */
    void clear();

    /**
     * clear handlers, but head, tail will not be remove
     */
    void reset();

    void bindTarget(T target);

    void unbindTarget();

    T getTarget();

    void inbound() throws Throwable;

    void outbound() throws Throwable;

    boolean hadOutbound();

    HandlerContext getCurrentHandlerContext();
    void setCurrentHandlerContext(HandlerContext handlerContext);
}
