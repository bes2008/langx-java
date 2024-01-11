package com.jn.langx.el.expression.value;

public class LongExpression extends NumberExpression<Long> {
    public LongExpression(){

    }

    public LongExpression(Long number){
        this();
        setValue(number);
    }
}