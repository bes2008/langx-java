package com.jn.langx;

import com.jn.langx.util.function.Supplier;

/**
 * 定义一个工厂接口，用于根据输入的参数类型I生成输出的参数类型O。
 * 这个接口继承自Supplier函数式接口，表示它可以根据提供的输入生成相应的输出
 *
 * @param <I> 输入参数的类型
 * @param <O> 输出结果的类型
 */
public interface Factory<I, O> extends Supplier<I, O> {
    /**
     * 根据提供的输入参数生成相应的输出结果
     *
     * @param input 输入参数，用于生成输出结果
     * @return 根据输入参数生成的输出结果
     */
    @Override
    O get(I input);
}

