package com.jn.langx.el.expression.value;

public class StringExpression extends ValueExpression<String> {
    public StringExpression(){

    }

    public StringExpression(String value){
        this();
        setValue(value);
    }
}
