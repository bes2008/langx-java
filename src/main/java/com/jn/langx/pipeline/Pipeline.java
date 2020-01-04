package com.jn.langx.pipeline;

public interface Pipeline<T> {
    void addFirst(Handler handler);
    void addLast(Handler handler);
    HeadHandlerContext getHead();
    void clear();
    void handle();
    void setTarget(T target);
    T getTarget();
}
