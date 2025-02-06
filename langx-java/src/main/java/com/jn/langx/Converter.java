package com.jn.langx;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.Function;

/**
 * Converter接口扩展了Function接口，用于定义类型转换的规则和逻辑
 * 它额外提供了一种方式来检查两种类型之间是否可以进行转换
 *
 * @param <I> 输入类型的参数
 * @param <O> 输出类型的参数
 */
public interface Converter<I, O> extends Function<I, O> {

    /**
     * 应用转换逻辑将输入对象转换为输出对象
     * 这个方法重写了Function接口的apply方法
     *
     * @param input 要转换的输入对象
     * @return 转换后的输出对象
     */
    @Override
    O apply(I input);

    /**
     * 检查给定的源类和目标类之间是否可以进行转换
     *
     * @param sourceClass  源类的Class对象，表示转换前的类型
     * @param targetClass  目标类的Class对象，表示转换后的类型
     * @return 如果可以进行转换则返回true，否则返回false
     */
    boolean isConvertible(@NonNull Class sourceClass, @NonNull Class targetClass);
}

