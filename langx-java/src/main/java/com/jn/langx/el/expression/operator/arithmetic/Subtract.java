package com.jn.langx.el.expression.operator.arithmetic;


import com.jn.langx.el.expression.Expressions;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.NumberExpression;
import com.jn.langx.el.expression.value.NumberResultExpression;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.Strings;

public class Subtract extends AbstractBinaryOperator<NumberResultExpression<Number>, NumberResultExpression<Number>, NumberResultExpression<Number>> {

    public Subtract() {
        setOperateSymbol("-");
    }

    @Override
    public String getOperateSymbol() {
        return Strings.isEmpty(operateSymbol) ? "-" : operateSymbol;
    }

    @Override
    public NumberResultExpression<Number> execute() {
        NumberExpression<Number> expression = new NumberExpression<Number>();
        Number leftResult = Expressions.getNumberResult(getLeft());
        Number rightResult = Expressions.getNumberResult(getRight());
        expression.setValue(Numbers.sub(leftResult, rightResult));
        return expression;
    }
}
