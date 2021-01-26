package com.jn.langx.expression.operator.arithmetic;


import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.BinaryOperator;

public interface ArithmeticOperator<Left extends Expression,Right extends Expression,Result extends Expression> extends BinaryOperator<Left, Right,Result> {
    @Override
    Result execute();
}
