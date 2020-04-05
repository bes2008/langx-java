package com.jn.langx.expression.operator.compare;


import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.BooleanExpression;
import com.jn.langx.expression.value.BooleanResultExpression;
import com.jn.langx.util.Strings;
import com.jn.langx.util.comparator.Compares;

/**
 * letter or equals
 * @param <E>
 */
public class LE<E> extends AbstractBinaryOperator<Expression<E>, Expression<E>, BooleanResultExpression> implements CompareOperator<Expression<E>, Expression<E>> {

    public LE() {
        setOperateSymbol("<=");
    }

    public LE(Expression<E> left, Expression<E> right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public LE(String operateSymbol, Expression<E> left, Expression<E> right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public String getOperateSymbol() {
        return Strings.isEmpty(operateSymbol) ? "<=" : operateSymbol;
    }


    @Override
    public BooleanResultExpression execute() {
        BooleanExpression expression = new BooleanExpression();
        expression.setValue(Compares.le(getLeft().execute(), getRight().execute()));
        return expression;
    }
}