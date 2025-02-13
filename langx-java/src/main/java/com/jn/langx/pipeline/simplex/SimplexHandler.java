package com.jn.langx.pipeline.simplex;

import com.jn.langx.util.function.Function;

/**
 * SimplexHandler接口继承自Function接口，用于定义一个简单的处理操作
 * 它是一个泛型接口，允许在处理输入和输出时指定类型
 *
 * @param <IN> 输入类型
 * @param <OUT> 输出类型
 */
public interface SimplexHandler<IN, OUT> extends Function<IN, OUT> {
    /**
     * 应用处理逻辑于输入项
     *
     * @param in 输入项，其类型为IN
     * @return 处理后的输出项，其类型为OUT
     */
    OUT apply(IN in);
}
