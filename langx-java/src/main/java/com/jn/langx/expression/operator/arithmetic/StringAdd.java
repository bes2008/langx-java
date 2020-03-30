package com.jn.langx.expression.operator.arithmetic;


import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.StringExpression;
import com.jn.langx.util.Strings;

public class StringAdd extends AbstractBinaryOperator<Expression<String>, Expression<String>, StringExpression> implements ArithmeticOperator<Expression<String>, Expression<String>, StringExpression> {

    public StringAdd() {
    }

    public StringAdd(Expression<String> left, Expression<String> right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public StringAdd(String operateSymbol, Expression<String> left, Expression<String> right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public String getOperateSymbol() {
        return Strings.isEmpty(operateSymbol) ? "+" : operateSymbol;
    }


    @Override
    public StringExpression execute() {
        StringExpression expression = new StringExpression();
        expression.setValue(getLeft().execute() + getRight().execute());
        return expression;
    }
}
