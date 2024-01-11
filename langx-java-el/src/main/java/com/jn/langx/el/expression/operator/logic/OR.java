package com.jn.langx.el.expression.operator.logic;


import com.jn.langx.el.expression.Expressions;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.BooleanResultExpression;

public class OR<E extends BooleanResultExpression, F extends BooleanResultExpression> extends AbstractBinaryOperator<E, F, Boolean> implements BinaryLogicOperator<E, F> {

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
    public Boolean execute() {
        boolean leftResult = Expressions.getBooleanResult(getLeft());
        boolean rightResult = Expressions.getBooleanResult(getRight());
        return leftResult || rightResult;
    }
}
