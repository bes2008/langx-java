package com.jn.langx.el.expression.operator;


import com.jn.langx.el.expression.Expression;

public interface UnaryOperator<E extends Expression<R>,R> extends Operator<R> {
    void setTarget(E target);
    E getTarget();

    @Override
    R execute();
}
