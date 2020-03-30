package com.jn.langx.expression.value;

public class DoubleExpression extends NumberExpression<Double> {
    public DoubleExpression(){

    }

    public DoubleExpression(Double number){
        this();
        setValue(number);
    }
}
