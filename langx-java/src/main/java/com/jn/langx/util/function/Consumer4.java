package com.jn.langx.util.function;

/**
 * 定义一个四元组消费者接口，它接受四个参数但不返回任何值
 * 消费者接口旨在对输入的参数执行某些操作或处理，常用于函数式编程
 *
 * @param <ARG1> 第一个参数类型
 * @param <ARG2> 第二个参数类型
 * @param <ARG3> 第三个参数类型
 * @param <ARG4> 第四个参数类型
 */
public interface Consumer4 <ARG1, ARG2, ARG3, ARG4> {
    /**
     * 执行消费操作的方法，接受四个参数
     *
     * @param arg1 第一个参数，类型为ARG1
     * @param arg2 第二个参数，类型为ARG2
     * @param arg3 第三个参数，类型为ARG3
     * @param arg4 第四个参数，类型为ARG4
     */
    void accept(ARG1 arg1, ARG2 arg2, ARG3 arg3, ARG4 arg4);
}
