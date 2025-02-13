package com.jn.langx.util.spi;

import com.jn.langx.Provider;

import java.util.Iterator;

/**
 * 服务提供者接口，扩展了Provider接口
 * 用于获取指定服务类的所有提供者实例的迭代器
 *
 * @param <T> 服务类型的泛型参数
 */
public interface ServiceProvider<T> extends Provider<Class<T>, Iterator<T>> {
    /**
     * 获取指定服务类的所有提供者实例的迭代器
     *
     * @param serviceClass 服务类的Class对象，用于指定所需的服务类型
     * @return 提供者实例的迭代器，通过该迭代器可以遍历所有提供者实例
     */
    @Override
    Iterator<T> get(Class<T> serviceClass);
}
