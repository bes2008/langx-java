package com.jn.langx.util.function.mapper;

import com.jn.langx.util.function.Mapper;

/**
 * StringLiteralMapper接口继承了Mapper接口，专门用于处理字符串到其他类型的映射转换
 * 它定义了一个函数式接口，用于将字符串字面量转换为其他类型的值
 *
 * @param <V> 表示可以转换成的值的类型，是一个泛型参数
 */
public interface StringLiteralMapper<V> extends Mapper<String,V> {
    /**
     * 将字符串转换为指定类型的值
     *
     * @param value 字符串字面量，作为转换的输入
     * @return 转换后的值，类型为V
     */
    @Override
    V apply(String value);
}

