package com.jn.langx.expression.operator.arithmetic;

import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.NumberExpression;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.Strings;

public class Multiple extends AbstractBinaryOperator<NumberExpression<Number>, NumberExpression<Number>, NumberExpression<Number>> implements Add<NumberExpression<Number>, NumberExpression<Number>, NumberExpression<Number>> {

    public Multiple() {
    }

    public Multiple(NumberExpression<Number> left, NumberExpression<Number> right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public Multiple(String operateSymbol, NumberExpression<Number> left, NumberExpression<Number> right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public String getOperateSymbol() {
        return Strings.isEmpty(operateSymbol) ? "*" : operateSymbol;
    }


    @Override
    public NumberExpression<Number> execute() {
        NumberExpression<Number> expression = new NumberExpression<Number>();
        expression.setValue(Numbers.mul(getLeft().execute(), getRight().execute()));
        return expression;
    }
}
