package com.jn.langx.el.expression.operator.arithmetic;


import com.jn.langx.el.expression.Expression;
import com.jn.langx.el.expression.operator.BinaryOperator;
import com.jn.langx.el.expression.value.NumberResultExpression;

@SuppressWarnings("rawtypes")
public interface ArithmeticOperator<Left extends Expression,Right extends Expression> extends BinaryOperator<Left, Right, Number>, NumberResultExpression<Number> {
    @Override
    Number execute();
}
