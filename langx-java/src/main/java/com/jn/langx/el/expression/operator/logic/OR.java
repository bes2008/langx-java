package com.jn.langx.el.expression.operator.logic;


import com.jn.langx.el.expression.Expressions;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.BooleanExpression;
import com.jn.langx.el.expression.value.BooleanResultExpression;

public class OR<E extends BooleanResultExpression, F extends BooleanResultExpression> extends AbstractBinaryOperator<E, F, BooleanResultExpression> implements BinaryLogicOperator<E, F> {

    public OR() {
        setOperateSymbol("||");
    }

    public OR(E left, F right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public OR(String operateSymbol, E left, F right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public BooleanResultExpression execute() {
        boolean leftResult = Expressions.getBooleanResult(getLeft());
        boolean rightResult = Expressions.getBooleanResult(getRight());
        return new BooleanExpression(leftResult || rightResult);
    }
}
