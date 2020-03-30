package com.jn.langx.expression.operator.logic;


import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.AbstractUnaryOperator;
import com.jn.langx.expression.value.BooleanExpression;
import com.jn.langx.expression.BooleanResultExpression;

public class Non<E extends Expression<BooleanResultExpression>> extends AbstractUnaryOperator<E, BooleanResultExpression> implements UnaryLogicOperator<E> {
    @Override
    public BooleanResultExpression execute() {
        return new BooleanExpression(!this.getTarget().execute().execute());
    }
}
