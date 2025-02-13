package com.jn.langx.util.function;

/**
 * Mapper2接口扩展了Function2接口，用于定义一个接受两个输入参数并产生一个输出结果的函数映射关系
 * 这个接口主要用于需要根据两个不同输入来计算或生成一个输出的场景
 *
 * @param <I1> 第一个输入参数的类型
 * @param <I2> 第二个输入参数的类型
 * @param <O> 输出结果的类型
 */
public interface Mapper2<I1, I2, O> extends Function2<I1, I2, O> {
    // 此处没有定义额外的方法，因此没有方法级别的注释
}

