package com.jn.langx.expression.value;

public class LongExpression extends NumberExpression<Long> {
    public LongExpression(){

    }

    public LongExpression(Long number){
        this();
        setValue(number);
    }
}