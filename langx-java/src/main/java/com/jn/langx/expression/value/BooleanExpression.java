package com.jn.langx.expression.value;


public class BooleanExpression extends ValueExpression<Boolean> implements BooleanResultExpression {

    public BooleanExpression(){

    }

    public BooleanExpression(boolean value){
        setValue(value);
    }

}
