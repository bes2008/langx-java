package com.jn.langx.expression.value;


import com.jn.langx.expression.BooleanResultExpression;

public class BooleanExpression extends ValueExpression<Boolean> implements BooleanResultExpression {

    public BooleanExpression(){

    }

    public BooleanExpression(boolean value){
        setValue(value);
    }

}
