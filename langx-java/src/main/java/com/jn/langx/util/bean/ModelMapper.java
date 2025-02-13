package com.jn.langx.util.bean;

/**
 * ModelMapper接口定义了将一种类型对象转换为另一种类型对象的映射操作
 * 它提供了一个通用的机制，用于在不同的数据模型之间进行转换
 *
 * @param <Source> 源对象的类型
 * @param <Target> 目标对象的类型
 */
public interface ModelMapper<Source, Target> {
    /**
     * 将源对象映射为目标对象
     *
     * @param a 源对象，它将被转换为目标类型的对象
     * @return 转换后的目标对象
     */
    Target map(Source a);
}
