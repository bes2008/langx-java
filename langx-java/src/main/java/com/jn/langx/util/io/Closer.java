package com.jn.langx.util.io;

import java.util.List;

/**
 * Closer接口定义了一个通用的关闭操作，用于在不同的上下文中关闭资源
 * 它允许在泛型参数I指定的类型上执行关闭操作，并通过applyTo方法指定可应用此关闭操作的类类型
 */
public interface Closer<I> {
    /**
     * 执行关闭操作的方法
     *
     * @param i 要关闭的资源，其类型由泛型参数I指定
     */
    void close(I i);

    /**
     * 获取当前关闭操作可以应用到的类类型的列表
     *
     * @return 一个包含Class对象的列表，表示此关闭操作可应用于哪些类型的实例
     */
    List<Class> applyTo();
}

