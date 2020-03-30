package com.jn.langx.expression.operator.logic;


import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.UnaryOperator;
import com.jn.langx.expression.BooleanResultExpression;

public interface UnaryLogicOperator<E extends Expression<BooleanResultExpression>> extends UnaryOperator<E, BooleanResultExpression> {
}
