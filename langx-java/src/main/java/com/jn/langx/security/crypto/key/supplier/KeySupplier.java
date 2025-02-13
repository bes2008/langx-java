package com.jn.langx.security.crypto.key.supplier;

import java.security.Key;

/**
 * KeySupplier接口用于定义一个供应商，该供应商可以根据三个输入参数生成一个键对象
 * 这个接口是泛型的，允许它在不同的上下文中被重用，以生成不同类型的关键字
 *
 * @param <I1> 第一个输入参数的类型
 * @param <I2> 第二个输入参数的类型
 * @param <I3> 第三个输入参数的类型
 * @param <O> 生成的键的类型，必须是Key接口的实现
 */
public interface KeySupplier<I1, I2, I3, O extends Key> {
    /**
     * 根据提供的三个输入参数生成一个键
     *
     * @param i1 第一个输入参数，其类型为I1
     * @param i2 第二个输入参数，其类型为I2
     * @param i3 第三个输入参数，其类型为I3
     * @return 返回一个由提供的输入参数生成的键，其类型为O
     */
    O get(I1 i1, I2 i2, I3 i3);
}
