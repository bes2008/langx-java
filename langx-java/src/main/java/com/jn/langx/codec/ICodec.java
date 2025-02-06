package com.jn.langx.codec;

/**
 * ICodec接口定义了编解码的标准，它继承了Encoder和Decoder接口，用于处理对象和字节数组之间的相互转换
 * 这个接口的存在确保了编解码过程的一致性和标准化，使得不同的编解码实现可以互换和兼容
 *
 * @param <T> 表示待编解码的对象类型
 */
public interface ICodec<T> extends Encoder<T,byte[]>, Decoder<byte[],T>{
    /**
     * 解码方法，将字节数组转换为指定类型对象
     * 主要用于将接收到的字节数据还原为业务对象，以便进行后续处理
     *
     * @param bytes 待解码的字节数组
     * @return 转换后的对象实例
     * @throws CodecException 当解码过程中发生错误时抛出此异常
     */
    @Override
    T decode(byte[] bytes) throws CodecException;

    /**
     * 编码方法，将指定类型对象转换为字节数组
     * 主要用于将业务对象转换为可传输或可存储的字节流
     *
     * @param obj 待编码的对象实例
     * @return 转换后的字节数组
     * @throws CodecException 当编码过程中发生错误时抛出此异常
     */
    @Override
    byte[] encode(T obj) throws CodecException;
}

