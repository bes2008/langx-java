package com.jn.langx.pipeline;

public interface Pipeline {
    void addFirst(Handler handler);
    void addLast(Handler handler);
    HeadHandlerContext getHead();
    void clear();
}
