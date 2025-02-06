package com.jn.langx;

/**
 * Delegatable接口定义了可以设置和获取代理对象的通用契约。
 * 它扩展了DelegateHolder接口，要求实现了getDelegate方法
 * 此接口旨在为那些希望将实际操作委托给另一个对象处理的类提供一个标准的访问方式
 *
 * @param <T> 代理对象的类型，允许通过泛型来约束和访问代理对象
 */
public interface Delegatable<T> extends DelegateHolder<T> {
    /**
     * 获取当前对象的代理
     *
     * @return 返回当前对象的代理实例，类型为T
     * @see DelegateHolder#getDelegate()
     */
    @Override
    T getDelegate();

    /**
     * 设置当前对象的代理
     *
     * @param delegate 要设置为当前对象代理的实例，类型为T
     *                 代理对象负责执行实际的操作或计算
     */
    void setDelegate(final T delegate);
}
