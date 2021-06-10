package com.jn.langx.codec;

public interface BinaryCodec extends BinaryEncoder, BinaryDecoder, ICodec<byte[]>{
    @Override
    byte[] decode(byte[] source) throws CodecException;

    @Override
    byte[] encode(byte[] source) throws CodecException;
}
