package com.jn.langx.el.expression.operator.logic;


import com.jn.langx.el.expression.Expression;
import com.jn.langx.el.expression.Expressions;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.BooleanResultExpression;

public class And<E extends Expression<BooleanResultExpression>, F extends Expression<BooleanResultExpression>> extends AbstractBinaryOperator<E, F, Boolean> implements BinaryLogicOperator<E, F> {
    public And() {
        setOperateSymbol("&&");
    }

    public And(E left, F right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public And(String operateSymbol, E left, F right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public Boolean execute() {
        boolean leftResult = Expressions.getBooleanResult(getLeft());
        boolean rightResult = Expressions.getBooleanResult(getRight());
        return leftResult && rightResult;
    }

}
