package com.jn.langx.el.expression.operator.logic;


import com.jn.langx.el.expression.Expression;
import com.jn.langx.el.expression.Expressions;
import com.jn.langx.el.expression.operator.AbstractUnaryOperator;

public class Non<E extends Expression<Boolean>> extends AbstractUnaryOperator<E, Boolean> implements UnaryLogicOperator<E,Boolean> {

    public Non() {
        setOperateSymbol("!");
    }

    public Non(E target) {
        setTarget(target);
    }

    public Non(String operateSymbol, E target) {
        this(target);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public String toString() {
        return getOperateSymbol() + " " + getTarget().toString();
    }

    @Override
    public Boolean execute() {
        boolean targetResult = Expressions.getBooleanResult(this.getTarget());
        return !targetResult;
    }
}
