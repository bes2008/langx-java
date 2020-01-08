package com.jn.langx;

import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.function.Supplier0;

public interface IdGenerator<E> extends Supplier0<String>, Supplier<E, String> {
    String get(E e);

    String get();
}
