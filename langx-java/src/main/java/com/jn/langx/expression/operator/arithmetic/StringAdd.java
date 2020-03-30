package com.jn.langx.expression.operator.arithmetic;


import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.StringExpression;

public class StringAdd extends AbstractBinaryOperator<StringExpression, StringExpression, StringExpression> implements ArithmeticOperator<StringExpression,StringExpression,StringExpression> {
    @Override
    public StringExpression execute() {
        StringExpression expression = new StringExpression();
        expression.setValue(getLeft().execute()+getRight().execute());
        return expression;
    }
}
