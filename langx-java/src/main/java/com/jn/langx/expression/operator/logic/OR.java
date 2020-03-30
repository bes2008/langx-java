package com.jn.langx.expression.operator.logic;


import com.jn.langx.expression.BooleanResultExpression;
import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.BooleanExpression;

public class OR<E extends BooleanResultExpression, F extends BooleanResultExpression> extends AbstractBinaryOperator<E, F, BooleanResultExpression> implements BinaryLogicOperator<E, F> {
    @Override
    public BooleanResultExpression execute() {
        return new BooleanExpression(getLeft().execute() || getRight().execute());
    }
}
