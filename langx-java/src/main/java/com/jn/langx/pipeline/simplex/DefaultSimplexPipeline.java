package com.jn.langx.pipeline.simplex;

import com.jn.langx.pipeline.DefaultPipeline;
import com.jn.langx.pipeline.Handler;
import com.jn.langx.pipeline.HeadHandlerContext;
import com.jn.langx.util.struct.Holder;

/**
 * 单向pipeline ，也称为 单工pipeline 。
 *
 * 这个是 基于 双工 Pipeline 实现，所以不能复用
 */
public class DefaultSimplexPipeline implements SimplexPipeline {
    private DefaultPipeline<Holder<Object>> delegate;

    public DefaultSimplexPipeline() {
        this.delegate = new DefaultPipeline<Holder<Object>>();
    }

    public DefaultSimplexPipeline(Handler handler) {
        this(handler, handler);
    }

    public DefaultSimplexPipeline(Handler headHandler, Handler tailHandler) {
        this.delegate = new DefaultPipeline<Holder<Object>>(headHandler, tailHandler);
    }

    public void addFirst(Handler handler) {
        this.delegate.addFirst(handler);
    }

    public void addLast(Handler handler) {
        this.delegate.addLast(handler);
    }

    public HeadHandlerContext getHead() {
        return this.delegate.getHead();
    }

    public void clear() {
        this.delegate.clear();
    }

    public void reset() {
        this.delegate.reset();
    }

    public Object handle(Object message) {
        this.delegate.bindTarget(new Holder<Object>(message));
        Holder<Object> resultHolder = getTarget();
        this.delegate.unbindTarget();
        return resultHolder.get();
    }

    public Holder<Object> getTarget() {
        return this.delegate.getTarget();
    }
}
