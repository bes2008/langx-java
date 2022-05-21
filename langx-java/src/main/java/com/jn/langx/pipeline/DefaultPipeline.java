package com.jn.langx.pipeline;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.struct.Holder;

public class DefaultPipeline<T> implements Pipeline<T> {
    private HeadHandlerContext head;
    private TailHandlerContext tail;
    private Holder<T> targetHolder = new Holder<T>(null);
    private HandlerContext current = null;

    public DefaultPipeline() {
        this(new HeadHandlerContext(), new TailHandlerContext());
    }

    public DefaultPipeline(Handler handler) {
        this(handler, handler);
    }

    public DefaultPipeline(Handler headHandler, Handler tailHandler) {
        this(new HeadHandlerContext(headHandler), new TailHandlerContext(tailHandler));
    }

    private DefaultPipeline(HeadHandlerContext head, TailHandlerContext tail) {
        this.head = head;
        this.tail = tail;
        this.head.setNext(tail);
        this.tail.setPrev(head);
        head.setPipeline(this);
        tail.setPipeline(this);
    }

    @Override
    public void addFirst(Handler handler) {
        HandlerContext ctx = new HandlerContext(handler);
        ctx.setPipeline(this);
        HandlerContext first = head.getNext();

        first.setPrev(ctx);
        ctx.setNext(first);

        head.setNext(ctx);
        ctx.setPrev(head);
    }

    @Override
    public void addLast(Handler handler) {
        HandlerContext ctx = new HandlerContext(handler);
        ctx.setPipeline(this);
        HandlerContext last = tail.getPrev();

        last.setNext(ctx);
        ctx.setPrev(last);

        ctx.setNext(tail);
        tail.setPrev(ctx);
    }

    @Override
    public HeadHandlerContext getHead() {
        return head;
    }

    @Override
    public void reset() {
        clearHandlers(false);
    }

    @Override
    public void clear() {
        clearHandlers(true);
    }

    private void clearHandlers(boolean removeHeadAndTail) {
        setCurrentHandlerContext(null);
        HandlerContext ctx = getHead();
        if (ctx != null) {
            while (ctx.hasNext()) {
                HandlerContext next = ctx.getNext();
                if (removeHeadAndTail) {
                    ctx.clear();
                } else {
                    if (head == ctx || tail == ctx) {
                        ctx.clear(false);
                    } else {
                        ctx.clear();
                    }
                }
                ctx = next;
            }
            if (removeHeadAndTail) {
                ctx.clear();
            } else {
                if (head == ctx || tail == ctx) {
                    ctx.clear(false);
                } else {
                    ctx.clear();
                }
            }
        }
        if (removeHeadAndTail) {
            this.head = null;
            this.tail = null;
        } else {
            setHeadHandler(head.getHandler());
            setTailHandler(tail.getHandler());
        }
        unbindTarget();
    }

    @Override
    public void inbound() throws Throwable {
        Preconditions.checkNotNull(targetHolder.isNull(), "target is null");
        getHead().inbound();
    }

    @Override
    public void inbound(T message) throws Throwable {
        bindTarget(message);
        inbound();
    }

    @Override
    public void outbound() throws Throwable {
        Preconditions.checkNotNull(this.targetHolder.isNull(), "target is null");
        Preconditions.checkNotNull(current, "current handler context is null");
        current.outbound();
    }

    @Override
    public void bindTarget(T target) {
        this.targetHolder.set(target);
    }

    @Override
    public void unbindTarget() {
        this.targetHolder.reset();
    }

    @Override
    public T getTarget() {
        return this.getTargetHolder().get();
    }

    @Override
    public Holder<T> getTargetHolder() {
        return this.targetHolder;
    }

    public void setHeadHandler(Handler headHandler) {
        Preconditions.checkTrue(head != null && !head.isSkiped() && !head.isInbounded() && !head.isOutbounded());
        HeadHandlerContext ctx = new HeadHandlerContext(headHandler);
        ctx.setPipeline(this);

        HandlerContext next = this.head == null ? null : this.head.getNext();
        if (next == null) {
            next = tail;
        }
        ctx.setNext(next);
        if (next != null) {
            next.setPrev(ctx);
        }

        this.head = ctx;
    }

    public void setTailHandler(Handler tailHandler) {
        Preconditions.checkTrue(tail != null && !tail.isSkiped() && !tail.isInbounded() && !tail.isOutbounded());
        TailHandlerContext ctx = new TailHandlerContext(tailHandler);
        ctx.setPipeline(this);
        HandlerContext prev = this.tail == null ? null : this.tail.getPrev();
        if (prev == null) {
            prev = head;
        }
        ctx.setPrev(prev);
        if (prev != null) {
            prev.setNext(ctx);
        }
        this.tail = ctx;
    }

    @Override
    public boolean hadOutbound() {
        return false;
    }

    @Override
    public HandlerContext getCurrentHandlerContext() {
        return current;
    }

    @Override
    public void setCurrentHandlerContext(HandlerContext handlerContext) {
        this.current = handlerContext;
    }
}
