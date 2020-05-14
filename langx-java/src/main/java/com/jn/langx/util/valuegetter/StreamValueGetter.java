package com.jn.langx.util.valuegetter;

import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;

import java.util.List;

public class StreamValueGetter<O> implements ValueGetter<Object, List<O>> {
    private Function<Object, O> mapper;

    public StreamValueGetter(Function mapper){
        this.mapper = mapper;
    }

    public StreamValueGetter(final ValueGetter mapping){
        this.mapper = new Function<Object, O>() {
            @Override
            public O apply(Object input) {
                return (O)mapping.get(input);
            }
        };
    }


    @Override
    public List<O> get(Object list) {
        return Pipeline.of(list).map(mapper).asList();
    }
}
