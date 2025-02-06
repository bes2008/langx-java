package com.jn.langx.codec;

/**
 * BinaryCodec接口定义了二进制数据编码和解码的契约
 * 它继承了BinaryEncoder和BinaryDecoder接口，实现了对二进制数据的编解码功能
 * 同时，作为ICodec<byte[]>的子接口，它专注于字节数组的编解码操作
 */
public interface BinaryCodec extends BinaryEncoder, BinaryDecoder, ICodec<byte[]>{
    /**
     * 解码二进制数据
     *
     * @param source 待解码的二进制数据源
     * @return 解码后的字节数组
     * @throws CodecException 当解码过程中发生错误时抛出此异常
     */
    @Override
    byte[] decode(byte[] source) throws CodecException;

    /**
     * 编码二进制数据
     *
     * @param source 待编码的二进制数据源
     * @return 编码后的字节数组
     * @throws CodecException 当编码过程中发生错误时抛出此异常
     */
    @Override
    byte[] encode(byte[] source) throws CodecException;
}
