package com.jn.langx.expression.operator;


import com.jn.langx.expression.Expression;

public interface UnaryOperator<E extends Expression<R>,R> extends Operator<R> {
    void setTarget(E target);
    E getTarget();
}
