package com.jn.langx.codec;

/**
 * @since 5.3.9
 */
public interface StringifyEncoder<T> extends Encoder<T,String> {
    @Override
    String encode(T source);
}

