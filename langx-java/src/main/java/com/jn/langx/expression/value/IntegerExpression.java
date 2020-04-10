package com.jn.langx.expression.value;

public class IntegerExpression extends NumberExpression<Integer> {
    public IntegerExpression(){

    }

    public IntegerExpression(Integer number){
        this();
        setValue(number);
    }
}
