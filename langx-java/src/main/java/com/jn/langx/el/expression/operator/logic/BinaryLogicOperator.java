package com.jn.langx.el.expression.operator.logic;


import com.jn.langx.el.expression.Expression;
import com.jn.langx.el.expression.operator.BinaryOperator;
import com.jn.langx.el.expression.value.BooleanResultExpression;

public interface BinaryLogicOperator<Left extends Expression, Right extends Expression> extends BinaryOperator<Left, Right, BooleanResultExpression> {
    @Override
    BooleanResultExpression execute();
}
