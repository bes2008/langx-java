package com.jn.langx.util.function;

/**
 * Mapper接口继承自Function接口，用于定义一个可以将输入类型I转换为输出类型O的映射器
 * 这个接口的主要目的是在数据处理过程中，提供一个标准方式来转换数据对象
 *
 * @param <I> 输入类型的泛型参数，表示转换前的数据类型
 * @param <O> 输出类型的泛型参数，表示转换后的数据类型
 */
public interface Mapper<I, O> extends Function<I, O> {

    /**
     * 应用转换逻辑将输入对象转换为输出对象
     *
     * @param input 输入对象，类型为I，即转换前的数据
     * @return 转换后的输出对象，类型为O
     */
    @Override
    O apply(I input);
}

