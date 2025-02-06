package com.jn.langx.invocation;

import java.lang.reflect.Constructor;

/**
 * ConstructorInvocation 接口继承自 Invocation 接口，用于 specifically 处理构造器（Constructor）的调用情况
 * 它提供了一个获取当前连接点（Join Point）的方法，此处的连接点特指正在被调用的构造器
 */
public interface ConstructorInvocation extends Invocation<Constructor> {
    /**
     * 获取当前的连接点，即正在被调用的构造器
     *
     * @return 正在被调用的构造器对象
     */
    @Override
    Constructor getJoinPoint();
}
