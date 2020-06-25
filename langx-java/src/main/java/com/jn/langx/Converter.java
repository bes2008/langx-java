package com.jn.langx;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.Function;

public interface Converter<I, O> extends Function<I, O> {
    boolean isConvertible(@NonNull Class sourceClass,@NonNull Class targetClass);
}

