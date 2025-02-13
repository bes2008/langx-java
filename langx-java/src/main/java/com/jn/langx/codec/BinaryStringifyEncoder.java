package com.jn.langx.codec;
/**
 * BinaryStringifyEncoder接口自StringifyEncoder接口继承而来，专用于字节数组的编码工作。
 * 它的主要作用是将字节数组转换为字符串形式，这在数据传输、存储或调试信息的可视化等方面非常有用。
 *
 * @since 5.3.9 表示该接口自版本5.3.9开始引入。
 */
public interface BinaryStringifyEncoder extends StringifyEncoder<byte[]>{
    /**
     * 编码字节数组为字符串。
     *
     * @param bytes 待编码的字节数组。
     * @return 编码后的字符串表示。
     */
    @Override
    String encode(byte[] bytes);
}
