package com.jn.langx.expression.value;

public class NumberExpression<E extends Number> extends ValueExpression<E> implements NumberResultExpression<E> {
    public NumberExpression(){

    }

    public NumberExpression(E number){
        this();
        setValue(number);
    }
}
