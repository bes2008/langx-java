package com.jn.langx.chain;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.struct.counter.IntegerCounter;
import com.jn.langx.util.struct.counter.SimpleIntegerCounter;
import com.jn.langx.util.struct.counter.ThreadLocalIntegerCounter;

import java.util.List;

/**
 * 类似于 Java EE  Servlet 规范中的 FilterChain。
 * 并且需要在 handler 的 handle方法内部 调用 chain.handle(req, resp)
 *
 * @param <REQ>
 * @param <RESP>
 */
public class FilterChain<REQ, RESP> extends AbstractChain<REQ, RESP> {
    private List<Handler<REQ, RESP>> handlers = Collects.emptyArrayList();
    private IntegerCounter posHolder;

    public FilterChain() {
        this(false);
    }

    public FilterChain(IntegerCounter posHolder) {
        this.posHolder = posHolder;
    }

    /**
     * 如果 是独占 chain，则 每一次使用chain时，需要新建一个独有的chain
     */
    public FilterChain(boolean shareChain) {
        if(shareChain){
            this.posHolder = new ThreadLocalIntegerCounter();
        }else{
            this.posHolder = new SimpleIntegerCounter();
        }
    }


    @Override
    public void handle(final REQ request, final RESP response) {
        int pos = posHolder.get();
        if (pos < handlers.size() - 1 && pos >= -1) {
            posHolder.increment();
            Handler<REQ, RESP> handler = handlers.get(pos);
            handler.handle(request, response, this);
        }

        if (pos >= this.handlers.size() - 1) {
            // 执行完毕后重置
            posHolder.set(-1);
        }
    }

    public void addHandler(@NonNull Handler<REQ, RESP> handler) {
        Preconditions.checkNotNull(handler);
        handlers.add(handler);
    }


}
