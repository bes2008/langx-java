package com.jn.langx.chain;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;

import java.util.List;

/**
 * 类似于 Java EE  Servlet 规范中的 FilterChain。 当然了，这个只是简易版本的，每次需要Chain的时候，需要重建。
 *
 * @param <REQ>
 * @param <RESP>
 */
public class FilterChain<REQ, RESP> extends AbstractChain<REQ, RESP> {
    private List<Handler<REQ, RESP>> handlers = Collects.emptyArrayList();
    int pos = 0;

    @Override
    public void handle(final REQ request, final RESP response) {
        if (pos < handlers.size() && pos >= 0) {
            Handler<REQ, RESP> handler = handlers.get(this.pos++);
            handler.handle(request, response, this);
        }
    }

    public void addHandler(@NonNull Handler<REQ, RESP> handler) {
        Preconditions.checkNotNull(handler);
        handlers.add(handler);
    }


}
