package com.jn.langx.pipeline;

public interface Pipeline<T> {
    void addFirst(Handler handler);
    void addLast(Handler handler);
    HeadHandlerContext getHead();
    void clear();
    void handle();
    void bindTarget(T target);
    void unbindTarget();
    T getTarget();
}
