package com.jn.langx.el.expression.operator.arithmetic;


import com.jn.langx.el.expression.Expressions;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.NumberResultExpression;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.Strings;

public class Subtract extends AbstractBinaryOperator<NumberResultExpression<Number>, NumberResultExpression<Number>, Number> implements ArithmeticOperator<NumberResultExpression<Number>, NumberResultExpression<Number>>{

    public Subtract() {
        setOperateSymbol("-");
    }

    @Override
    public String getOperateSymbol() {
        return Strings.isEmpty(operateSymbol) ? "-" : operateSymbol;
    }

    @Override
    public Number execute() {
        Number leftResult = Expressions.getNumberResult(getLeft());
        Number rightResult = Expressions.getNumberResult(getRight());
        Number result = Numbers.sub(leftResult, rightResult);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        NumberResultExpression<Number> left = getLeft();

        stringBuilder.append(left.toString());
        stringBuilder.append(" ").append(getOperateSymbol()).append(" ");

        NumberResultExpression<Number> right = getRight();
        boolean rightBrace = false;
        if (right instanceof ArithmeticOperator) {
            if (right instanceof Add || right instanceof Subtract) {
                rightBrace = true;
            }
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
