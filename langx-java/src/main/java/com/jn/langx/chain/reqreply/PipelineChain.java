package com.jn.langx.chain.reqreply;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.List;

/**
 * Pipeline 风格的 Chain，一直往后进行处理
 *
 * @param <REQ>
 * @param <RESP>
 */
public class PipelineChain<REQ, RESP> extends AbstractChain<REQ, RESP> {

    private List<Handler<REQ, RESP>> handlers = Collects.emptyArrayList();

    @Override
    public void handle(final REQ request, final RESP response) {
        Collects.forEach(handlers, new Consumer<Handler<REQ, RESP>>() {
            @Override
            public void accept(Handler<REQ, RESP> handler) {
                handler.handle(request, response, PipelineChain.this);
            }
        });
    }

    public void addHandler(@NonNull Handler<REQ, RESP> handler) {
        Preconditions.checkNotNull(handler);
        handlers.add(handler);
    }

}
