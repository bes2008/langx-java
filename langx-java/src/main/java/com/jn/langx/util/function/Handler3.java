package com.jn.langx.util.function;

/**
 * 定义一个处理三个不同类型输入参数的接口
 * 用于规范实现类必须提供处理三个特定类型参数的方法
 *
 * @param <I1> 第一个输入参数的类型
 * @param <I2> 第二个输入参数的类型
 * @param <I3> 第三个输入参数的类型
 */
public interface Handler3<I1,I2,I3> {
    /**
     * 处理三个不同类型的输入参数
     *
     * @param i1 第一个输入参数，类型为I1
     * @param i2 第二个输入参数，类型为I2
     * @param i3 第三个输入参数，类型为I3
     */
    void handle(I1 i1, I2 i2, I3 i3);
}

