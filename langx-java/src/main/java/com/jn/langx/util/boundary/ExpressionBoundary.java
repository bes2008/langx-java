package com.jn.langx.util.boundary;

public abstract class ExpressionBoundary implements Boundary{
    protected String expression;
    public ExpressionBoundary(){

    }
    public ExpressionBoundary(String expression){
        setExpression(expression);
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
