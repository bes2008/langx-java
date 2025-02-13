package com.jn.langx.util.struct;

import com.jn.langx.util.hash.Hashed;

/**
 * Reference接口定义了一个通用的引用类型，它扩展了Hashed接口
 * 这个接口旨在为各种类型的引用提供一个统一的标准，以便在不同的上下文中使用
 *
 * @param <T> 引用持有的对象类型
 */
public interface Reference<T> extends Hashed {
    /**
     * 获取引用指向的对象
     *
     * @return 引用指向的对象，如果引用为空，则返回null
     */
    T get();

    /**
     * 检查引用是否为空
     *
     * @return 如果引用为空，则返回true；否则返回false
     */
    boolean isNull();
}
