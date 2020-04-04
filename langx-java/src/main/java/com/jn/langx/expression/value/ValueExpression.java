package com.jn.langx.expression.value;


import com.jn.langx.expression.BaseExpression;
import com.jn.langx.expression.Expression;
import com.jn.langx.util.hash.HashCodeBuilder;

public class ValueExpression<E> extends BaseExpression<E> implements Expression<E> {
    private E value;

    public ValueExpression() {
    }

    public ValueExpression(E value) {
        setValue(value);
    }

    public void setValue(E value) {
        this.value = value;
    }

    @Override
    public E execute() {
        return value;
    }

    @Override
    public String toString() {
        return value == null ? "" : value.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().with(value).build();
    }
}
