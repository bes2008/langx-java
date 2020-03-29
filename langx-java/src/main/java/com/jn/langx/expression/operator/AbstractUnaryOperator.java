package com.jn.langx.expression.operator;


import com.jn.langx.expression.Expression;

public abstract class AbstractUnaryOperator<E extends Expression<R>,R> implements UnaryOperator<E,R> {
    protected E target;

    @Override
    public void setTarget(E target) {
        this.target = target;
    }

    @Override
    public E getTarget() {
        return this.target;
    }
}
