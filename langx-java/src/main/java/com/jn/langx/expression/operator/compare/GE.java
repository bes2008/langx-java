package com.jn.langx.expression.operator.compare;


import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.BooleanExpression;
import com.jn.langx.expression.value.BooleanResultExpression;
import com.jn.langx.util.Strings;
import com.jn.langx.util.comparator.Compares;

/**
 * grater or equals
 *
 * @param <E>
 */
public class GE<E> extends AbstractBinaryOperator<Expression<E>, Expression<E>, BooleanResultExpression> implements CompareOperator<Expression<E>, Expression<E>> {

    public GE() {
        setOperateSymbol(">=");
    }

    public GE(Expression<E> left, Expression<E> right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public GE(String operateSymbol, Expression<E> left, Expression<E> right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public String getOperateSymbol() {
        return Strings.isEmpty(operateSymbol) ? ">=" : operateSymbol;
    }


    @Override
    public BooleanResultExpression execute() {
        BooleanExpression expression = new BooleanExpression();
        expression.setValue(Compares.ge(getLeft().execute(), getRight().execute()));
        return expression;
    }
}
