package com.jn.langx.el.expression.operator.arithmetic;

import com.jn.langx.el.expression.Expressions;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.NumberResultExpression;
import com.jn.langx.util.Numbers;

public class Divide extends AbstractBinaryOperator<NumberResultExpression<Number>, NumberResultExpression<Number>, Number> implements ArithmeticOperator<NumberResultExpression<Number>, NumberResultExpression<Number>> {

    public Divide() {
        setOperateSymbol("/");
    }

    public Divide(NumberResultExpression<Number> left, NumberResultExpression<Number> right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public Divide(String operateSymbol, NumberResultExpression<Number> left, NumberResultExpression<Number> right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public Number execute() {
        Number leftResult = Expressions.getNumberResult(getLeft());
        Number rightResult = Expressions.getNumberResult(getRight());
        Number result = Numbers.div(leftResult, rightResult);
        return result;
    }

    @Override
    public String toString() {
        NumberResultExpression<Number> left = getLeft();

        StringBuilder stringBuilder = new StringBuilder();
        boolean leftBrace = false;
        if (left instanceof ArithmeticOperator) {
            if (left instanceof Add || left instanceof Subtract) {
                leftBrace = true;
            }
        }
        if (leftBrace) {
            stringBuilder.append("(");
        }
        stringBuilder.append(left.toString());
        if (leftBrace) {
            stringBuilder.append(")");
        }
        stringBuilder.append(" ").append(getOperateSymbol()).append(" ");

        NumberResultExpression<Number> right = getRight();
        boolean rightBrace = false;
        if (right instanceof ArithmeticOperator) {
            rightBrace = true;
        }
        if (rightBrace) {
            stringBuilder.append("(");
        }
        stringBuilder.append(right.toString());
        if (rightBrace) {
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }
}
