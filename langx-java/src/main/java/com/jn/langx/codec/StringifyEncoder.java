package com.jn.langx.codec;

/**
 * StringifyEncoder接口扩展了Encoder接口，专门用于将类型为T的对象编码为String类型。
 * 这个接口的存在使得需要将对象转换为字符串表示的场景变得更加简单和统一。
 *
 * @param <T> - 需要编码的对象类型。
 */
/**
 * @since 5.3.9
 */
public interface StringifyEncoder<T> extends Encoder<T,String> {
    /**
     * 将给定的源对象编码为字符串。
     *
     * @param source - 需要编码的源对象。
     * @return 编码后的字符串表示。
     */
    @Override
    String encode(T source);
}
