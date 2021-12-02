package com.jn.langx.el.expression.value;

public class DoubleExpression extends NumberExpression<Double> {
    public DoubleExpression(){

    }

    public DoubleExpression(Double number){
        this();
        setValue(number);
    }
}
