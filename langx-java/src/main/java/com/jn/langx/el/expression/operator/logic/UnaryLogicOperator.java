package com.jn.langx.el.expression.operator.logic;


import com.jn.langx.el.expression.Expression;
import com.jn.langx.el.expression.operator.UnaryOperator;
import com.jn.langx.el.expression.value.BooleanResultExpression;

public interface UnaryLogicOperator<E extends Expression<BooleanResultExpression>> extends UnaryOperator<E, BooleanResultExpression> {
    @Override
    BooleanResultExpression execute();
}
