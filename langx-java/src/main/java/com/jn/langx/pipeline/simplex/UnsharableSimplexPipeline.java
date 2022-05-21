package com.jn.langx.pipeline.simplex;

import com.jn.langx.pipeline.DefaultPipeline;
import com.jn.langx.pipeline.Handler;
import com.jn.langx.util.struct.Holder;

/**
 * 单向pipeline ，也称为 单工pipeline 。
 * <p>
 * 这个是 基于 双工 Pipeline 实现，所以不能复用, 每次使用完毕就无效了。
 */
public class UnsharableSimplexPipeline implements SimplexPipeline {
    private DefaultPipeline<Holder<Object>> delegate;

    public UnsharableSimplexPipeline() {
        this.delegate = new DefaultPipeline<Holder<Object>>();
    }

    public UnsharableSimplexPipeline(Handler handler) {
        this(handler, handler);
    }

    public UnsharableSimplexPipeline(Handler headHandler, Handler tailHandler) {
        this.delegate = new DefaultPipeline<Holder<Object>>(headHandler, tailHandler);
    }

    public void addFirst(SimplexHandler handler) {
        this.delegate.addFirst(new SimplexToDuplexHandlerAdapter(handler));
    }

    public void addLast(SimplexHandler handler) {
        this.delegate.addLast(new SimplexToDuplexHandlerAdapter(handler));
    }


    public void clear() {
        this.delegate.reset();
    }

    public Object handle(Object message) {
        this.delegate.bindTarget(new Holder<Object>(message));
        Holder<Object> resultHolder = getTargetHolder();
        this.delegate.unbindTarget();
        return resultHolder.get();
    }

    public Holder<Object> getTargetHolder() {
        return this.delegate.getTarget();
    }

}
