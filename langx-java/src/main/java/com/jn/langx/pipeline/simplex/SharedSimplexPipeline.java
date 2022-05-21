package com.jn.langx.pipeline.simplex;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.Holder;

import java.util.List;

public class SharedSimplexPipeline implements SimplexPipeline {
    private List<SimplexHandler> handlers = Collects.emptyArrayList();
    private boolean resultNullable = false;

    @Override
    public void addFirst(SimplexHandler handler) {
        this.handlers.add(handler);
    }

    @Override
    public void addLast(SimplexHandler handler) {
        this.handlers.add(handler);
    }

    @Override
    public void clear() {
        this.handlers.clear();
    }

    @Override
    public Object handle(final Object message) {
        final Holder<Object> result = new Holder<Object>(message);
        Collects.forEach(handlers, new Consumer<SimplexHandler>() {
            @Override
            public void accept(SimplexHandler handler) {
                handler.apply(result.get());
            }
        }, new Predicate<SimplexHandler>() {
            @Override
            public boolean test(SimplexHandler handler) {
                return result.isNull() && !resultNullable;
            }
        });
        return result.get();
    }


}
