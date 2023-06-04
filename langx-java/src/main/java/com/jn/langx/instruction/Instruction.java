package com.jn.langx.instruction;

/**
 * <pre>
 * 分为2种指令：
 *  1) 表达式
 *  2) 语句：
 *      2.1) 声明变量，赋值
 *      2.2) 函数调用
 * </pre>
 *
 * @see com.jn.langx.el.expression.Expression
 * @see Statement
 */
public interface Instruction<R> {
    Closure getClosure();

    void setClosure(Closure closure);

    <R> R execute();
}
