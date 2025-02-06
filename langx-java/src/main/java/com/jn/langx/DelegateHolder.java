package com.jn.langx;

/**
 * DelegateHolder接口定义了获取代理对象的方法
 * 它允许外部通过getDelegate方法访问内部持有的代理对象
 *
 * @param <T> 代理对象的类型，使得DelegateHolder可以类型化
 */
public interface DelegateHolder<T> {
    /**
     * 获取当前持有的代理对象
     *
     * @return 当前持有的代理对象，类型为T
     */
    T getDelegate();
}
