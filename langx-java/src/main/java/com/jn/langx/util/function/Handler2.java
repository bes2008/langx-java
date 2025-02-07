package com.jn.langx.util.function;

/**
 * 定义一个双参数的处理器接口
 * 用于处理两个不同类型的输入参数
 * 此接口的实现可以处理各种类型的组合，提高了代码的灵活性和可复用性
 *
 * @param <I1> 第一个输入参数的类型
 * @param <I2> 第二个输入参数的类型
 */
public interface Handler2<I1,I2> {
    /**
     * 处理两个输入参数的抽象方法
     * 实现此接口的类需要提供具体的处理逻辑
     *
     * @param i1 第一个输入参数，类型为I1
     * @param i2 第二个输入参数，类型为I2
     */
    void handle(I1 i1,I2 i2);
}

