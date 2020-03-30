package com.jn.langx.expression.operator.arithmetic;


import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.NumberExpression;
import com.jn.langx.util.Numbers;

public class Mod extends AbstractBinaryOperator<NumberExpression<Number>, NumberExpression<Number>, NumberExpression<Number>> implements Add<NumberExpression<Number>, NumberExpression<Number>, NumberExpression<Number>> {
    @Override
    public NumberExpression<Number> execute() {
        NumberExpression<Number> expression = new NumberExpression<Number>();
        expression.setValue(Numbers.mod(getLeft().execute(), getRight().execute()));
        return expression;
    }
}
