package com.jn.langx.util.function;

/**
 * Supplier2接口定义了一个双参数的供应者模型，用于生成一个输出值。
 * 这个接口代表了一个可以接受两个输入参数并产生一个输出的函数。
 * 主要用于在没有副作用的情况下，根据输入参数计算并返回一个结果。
 *
 * @param <I1> 第一个输入参数的类型。
 * @param <I2> 第二个输入参数的类型。
 * @param <O> 输出结果的类型。
 */
public interface Supplier2<I1, I2, O> {
    /**
     * 获取基于两个输入参数计算得到的输出结果。
     * 这个方法接受两个参数，并根据这两个参数计算返回一个结果。
     * 它是实现具体业务逻辑的地方，比如进行数学计算、数据处理等。
     *
     * @param i1 第一个输入参数，其类型为I1。
     * @param i2 第二个输入参数，其类型为I2。
     * @return 返回计算得到的输出结果，其类型为O。
     */
    O get(I1 i1, I2 i2);
}
