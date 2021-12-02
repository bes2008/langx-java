package com.jn.langx.el.expression.operator;


import com.jn.langx.el.expression.BaseExpression;
import com.jn.langx.el.expression.Expression;
import com.jn.langx.util.hash.HashCodeBuilder;

public abstract class AbstractUnaryOperator<E extends Expression<R>, R> extends BaseExpression<R> implements UnaryOperator<E, R> {
    private E target;
    protected String operateSymbol;

    @Override
    public void setOperateSymbol(String operateSymbol) {
        this.operateSymbol = operateSymbol;
    }

    @Override
    public String getOperateSymbol() {
        return this.operateSymbol;
    }

    @Override
    public void setTarget(E target) {
        this.target = target;
    }

    @Override
    public E getTarget() {
        return this.target;
    }

    @Override
    public String toString() {
        return operateSymbol + " " + target.toString();
    }

    public int hashCode() {
        return new HashCodeBuilder().with(operateSymbol).with(target).build();
    }
}
