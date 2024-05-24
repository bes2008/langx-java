package com.jn.langx.codec;

/**
 * @since 5.3.9
 */
public interface BinaryStringifyEncoder extends StringifyEncoder<byte[]>{
    @Override
    String encode(byte[] bytes);
}
