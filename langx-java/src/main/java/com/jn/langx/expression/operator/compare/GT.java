package com.jn.langx.expression.operator.compare;


import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.BooleanExpression;
import com.jn.langx.expression.value.BooleanResultExpression;
import com.jn.langx.util.comparator.Compares;

/**
 * grater than
 *
 * @param <E>
 */
public class GT<E> extends AbstractBinaryOperator<Expression<E>, Expression<E>, BooleanResultExpression> implements CompareOperator<Expression<E>, Expression<E>> {

    public GT() {
        setOperateSymbol(">");
    }

    public GT(Expression<E> left, Expression<E> right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public GT(String operateSymbol, Expression<E> left, Expression<E> right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }


    @Override
    public BooleanResultExpression execute() {
        BooleanExpression expression = new BooleanExpression();
        expression.setValue(Compares.gt(getLeft().execute(), getRight().execute()));
        return expression;
    }
}
