package com.jn.langx.expression.operator.logic;


import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.BooleanExpression;
import com.jn.langx.expression.value.BooleanResultExpression;

public class And<E extends Expression<BooleanResultExpression>, F extends Expression<BooleanResultExpression>> extends AbstractBinaryOperator<E, F, BooleanResultExpression> implements BinaryLogicOperator<E, F> {

    @Override
    public BooleanResultExpression execute() {
        return new BooleanExpression(left.execute().execute() && right.execute().execute());
    }

}
