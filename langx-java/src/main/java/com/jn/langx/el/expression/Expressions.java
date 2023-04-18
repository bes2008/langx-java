package com.jn.langx.el.expression;

public class Expressions {
    public static <N extends Number> N getNumberResult(Expression expression) {
        Object resultExpression = expression.execute();
        while (resultExpression instanceof Expression) {
            resultExpression = ((Expression) resultExpression).execute();
        }
        return (N) resultExpression;
    }

    public static boolean getBooleanResult(Expression expression) {
        Object resultExpression = expression.execute();
        while (resultExpression instanceof Expression) {
            resultExpression = ((Expression) resultExpression).execute();
        }
        return (Boolean) resultExpression;
    }
    private Expressions(){

    }
}
