package com.jn.langx.el.expression.operator.arithmetic;

import com.jn.langx.el.expression.Expressions;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.NumberExpression;
import com.jn.langx.el.expression.value.NumberResultExpression;
import com.jn.langx.util.Numbers;

public class Divide extends AbstractBinaryOperator<NumberResultExpression<Number>, NumberResultExpression<Number>, NumberResultExpression<Number>> {

    public Divide() {
        setOperateSymbol("/");
    }

    public Divide(NumberResultExpression<Number> left, NumberResultExpression<Number> right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public Divide(String operateSymbol, NumberResultExpression<Number> left, NumberResultExpression<Number> right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public NumberResultExpression<Number> execute() {
        NumberExpression<Number> expression = new NumberExpression<Number>();
        Number leftResult = Expressions.getNumberResult(getLeft());
        Number rightResult = Expressions.getNumberResult(getRight());
        expression.setValue(Numbers.div(leftResult, rightResult));
        return expression;
    }
}
