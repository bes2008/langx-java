package com.jn.langx.text.placeholder;

import com.jn.langx.util.function.Consumer3;
import com.jn.langx.util.struct.Holder;

/**
 * <pre>
 * PlaceholderSubExpressionHandler 接口定义了处理占位符子表达式的处理程序的标准行为。
 * 它继承自 Consumer3 函数式接口，用于处理三个参数的消费操作。
 * 该接口的主要作用是处理模板字符串中的占位符表达式，并将解析后的值存储到指定的持有者中。
 * </pre>
 */
public interface PlaceholderSubExpressionHandler extends Consumer3<String, String, Holder<String>> {

    /**
     * <pre>
     * 处理一个占位符表达式。
     * 此方法接收一个变量名、一个表达式以及一个变量值的持有者。
     * 它的目的是根据表达式解析出变量值，并将该值设置到变量值持有者中。
     * </pre>
     *
     * @param variable 变量名，用于标识模板中的占位符。
     * @param expression 表达式字符串，需要解析以获取变量的实际值。
     * @param variableValueHolder 一个持有者对象，用于存储解析后的变量值。
     */
    @Override
    void accept(String variable, String expression, Holder<String> variableValueHolder);
}
