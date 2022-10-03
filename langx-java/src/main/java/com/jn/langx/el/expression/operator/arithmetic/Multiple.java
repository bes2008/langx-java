package com.jn.langx.el.expression.operator.arithmetic;

import com.jn.langx.el.expression.Expressions;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.NumberExpression;
import com.jn.langx.el.expression.value.NumberResultExpression;
import com.jn.langx.util.Numbers;

public class Multiple extends AbstractBinaryOperator<NumberResultExpression<Number>, NumberResultExpression<Number>, NumberResultExpression<Number>> {

    public Multiple() {
        setOperateSymbol("*");
    }

    @Override
    public NumberResultExpression<Number> execute() {
        NumberExpression<Number> expression = new NumberExpression<Number>();
        Number leftResult = Expressions.getNumberResult(getLeft());
        Number rightResult = Expressions.getNumberResult(getRight());
        expression.setValue(Numbers.mul(leftResult, rightResult));
        return expression;
    }
}
