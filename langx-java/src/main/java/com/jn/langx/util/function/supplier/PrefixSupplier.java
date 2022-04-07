package com.jn.langx.util.function.supplier;

import com.jn.langx.util.function.Supplier;

/**
 * @since 4.4.7
 */
public interface PrefixSupplier extends Supplier<Object,String> {
    @Override
    String get(Object supplement);
}
