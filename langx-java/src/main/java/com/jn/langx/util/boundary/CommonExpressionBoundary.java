package com.jn.langx.util.boundary;

public class CommonExpressionBoundary extends CommonBoundary implements ExpressionAware{
    private String expression;

    public CommonExpressionBoundary(){

    }
    public CommonExpressionBoundary(String expression){
        setExpression(expression);
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return expression;
    }


}
