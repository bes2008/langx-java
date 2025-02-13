package com.jn.langx.codec;

/**
 * StringCodec接口继承了Encoder和Decoder接口，用于定义字符串到字符串的编解码规范
 * 它提供了encode和decode方法的具体实现，使得开发者可以专注于字符串数据的转换
 * 而不必关心底层的数据结构或格式细节
 */
public interface StringCodec extends Encoder<String,String>, Decoder<String,String>{
    /**
     * 解码字符串数据
     *
     * @param source 待解码的字符串
     * @return 解码后的字符串
     * @throws CodecException 如果解码过程中发生错误
     */
    @Override
    String decode(String source) throws CodecException;

    /**
     * 编码字符串数据
     *
     * @param source 待编码的字符串
     * @return 编码后的字符串
     * @throws CodecException 如果编码过程中发生错误
     */
    @Override
    String encode(String source) throws CodecException;
}
