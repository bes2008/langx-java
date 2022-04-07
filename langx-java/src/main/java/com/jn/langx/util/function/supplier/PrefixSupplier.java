package com.jn.langx.util.function.supplier;

import com.jn.langx.util.function.Supplier;

public interface PrefixSupplier extends Supplier<Object,String> {
    @Override
    String get(Object supplement);
}
