package com.jn.langx.util.struct;

/**
 * ValueHolder接口扩展了Ref接口，用于定义一个可以设置、重置和获取值的持有者
 * 这个接口的主要作用是提供一个标准的方法来操作和管理一个值的状态
 * 它允许用户更新值（通过set方法），恢复到初始状态（通过reset方法），以及获取当前值（通过get方法）
 *
 * @param <T> 泛型参数，表示ValueHolder将要持有的值的类型
 */
public interface ValueHolder<T> extends Ref<T> {

    /**
     * 重置ValueHolder到初始状态
     * 这个方法的目的是将ValueHolder恢复到其初始状态，可能包括清除当前值或者将其设置为默认值
     */
    void reset();
}
