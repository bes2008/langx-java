package com.jn.langx.el.expression.operator.compare;


import com.jn.langx.el.expression.Expression;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.BooleanExpression;
import com.jn.langx.el.expression.value.BooleanResultExpression;
import com.jn.langx.util.comparator.Compares;

/**
 * letter or equals
 *
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
    public BooleanResultExpression execute() {
        BooleanExpression expression = new BooleanExpression();
        expression.setValue(Compares.le(getLeft().execute(), getRight().execute()));
        return expression;
    }
}