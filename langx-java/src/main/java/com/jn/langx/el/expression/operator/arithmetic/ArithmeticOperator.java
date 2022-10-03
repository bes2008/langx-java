package com.jn.langx.el.expression.operator.arithmetic;


import com.jn.langx.el.expression.Expression;
import com.jn.langx.el.expression.operator.BinaryOperator;

public interface ArithmeticOperator<Left extends Expression,Right extends Expression> extends BinaryOperator<Left, Right, Number> {
    @Override
    Number execute();
}
