package com.jn.langx.util.function;

/**
 * Operator2接口继承自Function2，表示一个接受两个相同类型参数并返回相同类型结果的操作
 * 这个接口的目的是为了定义一个可以对两个相同类型的操作数进行操作并产生相同类型结果的函数式接口
 * 主要用于需要对两个操作数执行相同类型转换或操作的场景
 *
 * @param <V> 表示操作涉及的值的类型
 */
public interface Operator2<V> extends Function2<V, V, V> {
    // 此接口没有定义额外的方法，因为它继承了Function2接口，并使用了Function2的抽象方法来实现两个参数到一个结果的映射
}
