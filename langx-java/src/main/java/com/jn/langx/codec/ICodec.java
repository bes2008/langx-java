package com.jn.langx.codec;

public interface ICodec<T> extends Encoder<T,byte[]>, Decoder<byte[],T>{
    @Override
    T decode(byte[] bytes) throws CodecException;

    @Override
    byte[] encode(T obj) throws CodecException;
}
