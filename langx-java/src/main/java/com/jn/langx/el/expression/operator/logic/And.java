package com.jn.langx.el.expression.operator.logic;


import com.jn.langx.el.expression.Expression;
import com.jn.langx.el.expression.operator.AbstractBinaryOperator;
import com.jn.langx.el.expression.value.BooleanExpression;
import com.jn.langx.el.expression.value.BooleanResultExpression;

public class And<E extends Expression<BooleanResultExpression>, F extends Expression<BooleanResultExpression>> extends AbstractBinaryOperator<E, F, BooleanResultExpression> implements BinaryLogicOperator<E, F> {
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
    public BooleanResultExpression execute() {
        return new BooleanExpression(getLeft().execute().execute() && getRight().execute().execute());
    }

}
