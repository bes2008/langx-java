package com.jn.langx.el.expression.operator.logic;


import com.jn.langx.el.expression.Expression;
import com.jn.langx.el.expression.operator.UnaryOperator;

public interface UnaryLogicOperator<E extends Expression<Result>,Result> extends UnaryOperator<E, Result> {
    @Override
    Result execute();
}
