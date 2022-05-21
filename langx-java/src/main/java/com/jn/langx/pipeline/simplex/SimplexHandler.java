package com.jn.langx.pipeline.simplex;

import com.jn.langx.util.function.Function;

public interface SimplexHandler<IN, OUT> extends Function<IN, OUT> {
    OUT apply(IN in);
}
