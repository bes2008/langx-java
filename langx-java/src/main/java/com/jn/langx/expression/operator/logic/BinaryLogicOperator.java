package com.jn.langx.expression.operator.logic;


import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.BinaryOperator;
import com.jn.langx.expression.value.BooleanResultExpression;

public interface BinaryLogicOperator<Left extends Expression, Right extends Expression> extends BinaryOperator<Left, Right, BooleanResultExpression> {

}
