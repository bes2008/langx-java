package com.jn.langx.el.expression.value;


public class BooleanExpression extends ValueExpression<Boolean> implements BooleanResultExpression {

    public BooleanExpression(){

    }

    public BooleanExpression(boolean value){
        setValue(value);
    }

}
