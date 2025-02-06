package com.jn.langx;

/**
 * Formatter接口定义了一个用于格式化输入的通用模板。
 * 它允许通过指定的格式化规则将输入对象转换为输出对象
 *
 * @param <I> 输入类型的泛型参数
 * @param <O> 输出类型的泛型参数
 */
public interface Formatter<I, O> {
    /**
     * 格式化输入对象
     *
     * @param input 待格式化的输入对象
     * @param args 可变参数，用于指定格式化的参数
     * @return 格式化后的输出对象
     */
    O format(I input, Object... args);
}
